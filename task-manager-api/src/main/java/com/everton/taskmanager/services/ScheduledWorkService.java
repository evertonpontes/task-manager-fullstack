package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.task.SaveScheduledWorksDTO;
import com.everton.taskmanager.dtos.task.ScheduledWorkResponseDTO;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.TaskMapper;
import com.everton.taskmanager.repositories.ScheduledWorkRepository;
import com.everton.taskmanager.repositories.TaskRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledWorkService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ScheduledWorkRepository scheduledWorkRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public ScheduledWorkResponseDTO createScheduledWork(String taskId, SaveScheduledWorksDTO scheduledWorksDTO) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a scheduled work in this task.");
        }

        Double sortIndex = scheduledWorkRepository.findMaxSortIndexByTask(taskId);

        ScheduledWork scheduledWork = taskMapper.saveScheduledWorksDTOToScheduledWorks(scheduledWorksDTO);
        scheduledWork.setTask(task);
        scheduledWork.setSortIndex(sortIndex + 1);

        if (scheduledWorksDTO.assigneeEmail() == null || scheduledWorksDTO.assigneeEmail().isEmpty()) {
            scheduledWork.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(scheduledWorksDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));

            scheduledWork.setAssignee(assignee);
        }

        task.getScheduledWorks().add(scheduledWork);

        return taskMapper.scheduledWorkToScheduledWorkResponseDTO(scheduledWorkRepository.save(scheduledWork));
    }

    public List<ScheduledWorkResponseDTO> findAllScheduledWorksInTask (String taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view scheduled works in this task.");
        }

        List<ScheduledWork> scheduledWorks = task.getScheduledWorks();

        return scheduledWorks.stream()
                .map(taskMapper::scheduledWorkToScheduledWorkResponseDTO)
                .toList();
    }

    public ScheduledWorkResponseDTO updateScheduled(String scheduledWorkId, SaveScheduledWorksDTO scheduledWorksDTO) {
        ScheduledWork scheduledWork = scheduledWorkRepository
                .findById(scheduledWorkId)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled work not found."));

        Organization organization = scheduledWork.getTask().getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update a scheduled work in this task.");
        }

        taskMapper.updateScheduledWorksFromDTO(scheduledWorksDTO, scheduledWork);

        if (scheduledWorksDTO.assigneeEmail() != null && !scheduledWorksDTO.assigneeEmail().isEmpty()) {
            User assignee = userRepository.findByEmail(scheduledWorksDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            scheduledWork.setAssignee(assignee);
        }

        return taskMapper.scheduledWorkToScheduledWorkResponseDTO(scheduledWorkRepository.save(scheduledWork));
    }

    public void deleteScheduledWork(String scheduledWorkId) {
        ScheduledWork scheduledWork = scheduledWorkRepository
                .findById(scheduledWorkId)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled work not found."));

        Organization organization = scheduledWork.getTask().getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete a Scheduled work in this task.");
        }

        scheduledWorkRepository.delete(scheduledWork);
    }
}
