package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.task.LoggedTimeResponseDTO;
import com.everton.taskmanager.dtos.task.SaveLoggedTimeDTO;
import com.everton.taskmanager.dtos.task.SaveScheduledWorksDTO;
import com.everton.taskmanager.dtos.task.ScheduledWorkResponseDTO;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.tasks.tasktiming.LoggedTime;
import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.TaskMapper;
import com.everton.taskmanager.repositories.LoggedTimeRepository;
import com.everton.taskmanager.repositories.TaskRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedTimeService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LoggedTimeRepository loggedTimeRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public LoggedTimeResponseDTO createLoggedTime(String taskId, SaveLoggedTimeDTO loggedTimeDTO) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a Logged time in this task.");
        }

        Double sortIndex = loggedTimeRepository.findMaxSortIndexByTask(taskId);

        LoggedTime loggedTime = taskMapper.saveLoggedTimeDTOToLoggedTime(loggedTimeDTO);
        loggedTime.setSortIndex(sortIndex + 1);
        loggedTime.setTask(task);

        if (loggedTimeDTO.assigneeEmail() == null || loggedTimeDTO.assigneeEmail().isEmpty()) {
            loggedTime.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(loggedTimeDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));

            loggedTime.setAssignee(assignee);
        }
        task.getLoggedTimes().add(loggedTime);

        return taskMapper.loggedTimeToLoggedTimeResponseDTO(loggedTimeRepository.save(loggedTime));
    }

    public List<LoggedTimeResponseDTO> findAllLoggedTimesInTask (String taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view Logged times in this task.");
        }
        List<LoggedTime> loggedTimes = task.getLoggedTimes();

        return loggedTimes.stream()
                .map(taskMapper::loggedTimeToLoggedTimeResponseDTO)
                .toList();
    }

    public LoggedTimeResponseDTO updateLoggedTime(String loggedTimeId, SaveLoggedTimeDTO loggedTimeDTO) {
        LoggedTime loggedTime = loggedTimeRepository
                .findById(loggedTimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Logged time not found."));

        Organization organization = loggedTime.getTask().getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update a Logged time in this task.");
        }

        taskMapper.updateLoggedTimeFromDTO(loggedTimeDTO, loggedTime);

        if (loggedTimeDTO.assigneeEmail() != null && !loggedTimeDTO.assigneeEmail().isEmpty()) {
            User assignee = userRepository.findByEmail(loggedTimeDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            loggedTime.setAssignee(assignee);
        }

        return taskMapper.loggedTimeToLoggedTimeResponseDTO(loggedTimeRepository.save(loggedTime));
    }

    public void deleteLoggedTime(String loggedTimeId) {
        LoggedTime loggedTime = loggedTimeRepository
                .findById(loggedTimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Logged time not found."));

        Organization organization = loggedTime.getTask().getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete a Logged time in this task.");
        }

        loggedTimeRepository.delete(loggedTime);
    }

}
