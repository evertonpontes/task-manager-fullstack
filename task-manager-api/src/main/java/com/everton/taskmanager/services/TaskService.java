package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.task.SaveTaskDTO;
import com.everton.taskmanager.dtos.task.TaskResponseDTO;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.teams.Team;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.TaskMapper;
import com.everton.taskmanager.repositories.*;
import com.everton.taskmanager.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public TaskResponseDTO createTask(String organizationId, SaveTaskDTO taskDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a task in this organization.");
        }

        Double sortIndex = taskRepository.findMaxSortIndexByOrganization(organizationId);

        Task task = taskMapper.saveTaskDTOToTask(taskDTO);
        task.setOrganization(organization);
        task.setSortIndex(sortIndex + 1);

        if (taskDTO.assigneeEmail() == null || taskDTO.assigneeEmail().isEmpty()) {
            task.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(taskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            task.setAssignee(assignee);
        }

        return taskMapper.taskToTaskResponseDTO(taskRepository.save(task));
    }

    public TaskResponseDTO createTaskInTeam(String teamId, SaveTaskDTO taskDTO) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a task in this team.");
        }

        Double sortIndex = taskRepository.findMaxSortIndexByTeam(teamId);

        Task task = taskMapper.saveTaskDTOToTask(taskDTO);
        task.setOrganization(organization);
        task.setTeam(team);
        task.setSortIndex(sortIndex + 1);

        if (taskDTO.assigneeEmail() == null || taskDTO.assigneeEmail().isEmpty()) {
            task.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(taskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            task.setAssignee(assignee);
        }

        return taskMapper.taskToTaskResponseDTO(taskRepository.save(task));
    }

    public TaskResponseDTO createTaskInProject(String projectId, SaveTaskDTO taskDTO) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a task in this project.");
        }

        Double sortIndex = taskRepository.findMaxSortIndexByProject(projectId);

        Task task = taskMapper.saveTaskDTOToTask(taskDTO);
        task.setOrganization(organization);
        task.setProject(project);
        task.setSortIndex(sortIndex + 1);

        if (taskDTO.assigneeEmail() == null || taskDTO.assigneeEmail().isEmpty()) {
            task.setAssignee(authenticatedUser);
        } else {
            User assignee = userRepository.findByEmail(taskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            task.setAssignee(assignee);
        }

        return taskMapper.taskToTaskResponseDTO(taskRepository.save(task));
    }

    public List<TaskResponseDTO> findAllTasks (String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view tasks in this organization.");
        }

        List<Task> tasks = organization.getTasks();

        return tasks.stream()
                .map(taskMapper::taskToTaskResponseDTO)
                .toList();
    }

    public List<TaskResponseDTO> findAllTasksInTeam (String teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view tasks in this team.");
        }

        List<Task> tasks = team.getTasks();

        return tasks.stream()
                .map(taskMapper::taskToTaskResponseDTO)
                .toList();
    }

    public List<TaskResponseDTO> findAllTasksInProject (String projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view tasks in this project.");
        }

        List<Task> tasks = project.getTasks();

        return tasks.stream()
                .map(taskMapper::taskToTaskResponseDTO)
                .toList();
    }

    public TaskResponseDTO findTaskById(String taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view this task.");
        }

        return taskMapper.taskToTaskResponseDTO(task);
    }

    public TaskResponseDTO updateTask(String taskId, SaveTaskDTO taskDTO) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();
        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update this task.");
        }
        taskMapper.updateTaskFromDTO(taskDTO, task);

        if (taskDTO.assigneeEmail() != null && !taskDTO.assigneeEmail().isEmpty()) {
            User assignee = userRepository.findByEmail(taskDTO.assigneeEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
            task.setAssignee(assignee);
        }

        return taskMapper.taskToTaskResponseDTO(taskRepository.save(task));
    }

    public void deleteTask(String taskId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Organization organization = task.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete this task.");
        }

        taskRepository.delete(task);
    }
}
