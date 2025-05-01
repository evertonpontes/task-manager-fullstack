package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.task.SaveSubTaskDTO;
import com.everton.taskmanager.dtos.task.SubTaskResponseDTO;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.tasks.SubTask;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.TaskMapper;
import com.everton.taskmanager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public SubTaskResponseDTO createSubTask(String taskId, SaveSubTaskDTO subTaskDTO) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a sub task in this task.");
        }

        Double sortIndex = subTaskRepository.findMaxSortIndexByTask(taskId);

        SubTask subTask = taskMapper.saveSubTaskDTOToSubTask(subTaskDTO);
        subTask.setTask(task);
        subTask.setSortIndex(sortIndex + 1);

        if (subTaskDTO.assigneeEmail() == null || subTaskDTO.assigneeEmail().isEmpty()) {
            subTask.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(subTaskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            subTask.setAssignee(assignee);
        }

        task.getSubTasks().add(subTask);

        return taskMapper.subTaskToSubTaskResponseDTO(subTaskRepository.save(subTask));
    }

    public List<SubTaskResponseDTO> findAllSubTasksInTask (String taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view sub tasks in this task.");
        }
        List<SubTask> subTasks = task.getSubTasks();

        return subTasks.stream()
                .map(taskMapper::subTaskToSubTaskResponseDTO)
                .toList();
    }

    public SubTaskResponseDTO updateSubTask(String subTaskId, SaveSubTaskDTO subTaskDTO) {
        SubTask subTask = subTaskRepository
                .findById(subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Sub task not found."));

        Organization organization = subTask.getTask().getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update a sub task in this task.");
        }
        taskMapper.updateSubTaskFromDTO(subTaskDTO, subTask);

        if (subTaskDTO.assigneeEmail() != null && !subTaskDTO.assigneeEmail().isEmpty()) {
            User assignee = userRepository.findByEmail(subTaskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            subTask.setAssignee(assignee);
        }

        return taskMapper.subTaskToSubTaskResponseDTO(subTaskRepository.save(subTask));
    }

    public void deleteSubTask(String subTaskId) {
        SubTask subTask = subTaskRepository
                .findById(subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Sub task not found."));

        Organization organization = subTask.getTask().getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete a sub task in this task.");
        }

        subTaskRepository.delete(subTask);
    }
}
