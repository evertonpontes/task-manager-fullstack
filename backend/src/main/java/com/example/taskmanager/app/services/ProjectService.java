package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.attributes.SaveTaskStatusRequestDTO;
import com.example.taskmanager.app.dtos.attributes.SaveTaskTypeRequestDTO;
import com.example.taskmanager.app.dtos.project.CreateProjectRequestDTO;
import com.example.taskmanager.app.dtos.project.ProjectResponseDTO;
import com.example.taskmanager.app.dtos.project.UpdateProjectRequestDTO;
import com.example.taskmanager.app.entities.Folder;
import com.example.taskmanager.app.entities.Project;
import com.example.taskmanager.app.entities.TaskStatus;
import com.example.taskmanager.app.entities.TaskType;
import com.example.taskmanager.app.mappers.ProjectMapper;
import com.example.taskmanager.app.repositories.FolderRepository;
import com.example.taskmanager.app.repositories.ProjectRepository;
import com.example.taskmanager.app.repositories.TaskStatusRepository;
import com.example.taskmanager.app.repositories.TaskTypeRepository;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.taskmanager.utils.exceptions.UnauthorizedAccessException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthService authService;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final FolderRepository folderRepository;

    public List<ProjectResponseDTO> getAllUserProjects() {
        User user = authService.getAuthenticatedUser();
        List<Project> projects = projectRepository.findAllByUserId(user.getId());
        return projects.stream()
                .map(projectMapper::projectToProjectResponseDTO)
                .toList();
    }

    public List<ProjectResponseDTO> getAllProjectsByFolderId(UUID folderId) {
        User user = authService.getAuthenticatedUser();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + folderId));

        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this folder");
        }

        List<Project> projects = folder.getProjects();

        return projects.stream()
                .map(projectMapper::projectToProjectResponseDTO)
                .toList();
    }
    
    public ProjectResponseDTO getProjectById(UUID projectId) {
        User user = authService.getAuthenticatedUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
                
        if (!project.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this project");
        }
        
        return projectMapper.projectToProjectResponseDTO(project);
    }

    public ProjectResponseDTO updateProject(UUID projectId, UpdateProjectRequestDTO request) {
        User user = authService.getAuthenticatedUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
                
        if (!project.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update this project");
        }
        
        // Update the project name if it's provided and different
        if (request.name() != null && !request.name().isBlank() && !request.name().equals(project.getName())) {
            project.setName(request.name().trim());
        }
        
        // Save the updated project
        Project updatedProject = projectRepository.save(project);
        return projectMapper.projectToProjectResponseDTO(updatedProject);
    }
    
    public void deleteProject(UUID projectId) {
        User user = authService.getAuthenticatedUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
                
        if (!project.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete this project");
        }
        
        projectRepository.delete(project);
    }

    public ProjectResponseDTO create(CreateProjectRequestDTO request) {
        String name = request.name().trim();

        User user = authService.getAuthenticatedUser();

        BigDecimal maxSortIndex = projectRepository.findTopByUserIdSortBySortIndexDesc(user.getId())
                .orElse(BigDecimal.valueOf(-1.0)); // Get the highest sort index
        BigDecimal newSortIndex = maxSortIndex.add(BigDecimal.valueOf(1.0));

        Project project = Project.builder()
                .name(request.name())
                .user(user)
                .sortIndex(newSortIndex)
                .build();

        // Save the project first to generate an ID
        Project savedProject = projectRepository.save(project);

        if (!request.taskTypes().isEmpty()) {
            for (SaveTaskStatusRequestDTO taskStatusRequestDTO : request.taskStatuses()) {
                BigDecimal maxTaskStatusSortIndex = taskStatusRepository.findTopByProjectIdSortBySortIndexDesc(savedProject.getId())
                        .orElse(BigDecimal.valueOf(-1.0));
                BigDecimal newTaskStatusSortIndex = maxTaskStatusSortIndex.add(BigDecimal.valueOf(1.0));

                TaskStatus taskStatus = TaskStatus.builder()
                        .name(taskStatusRequestDTO.name())
                        .project(savedProject)
                        .sortIndex(newTaskStatusSortIndex)
                        .build();
                savedProject.getTaskStatuses().add(taskStatus);
            }
        }

        if (!request.taskTypes().isEmpty()) {
            for (SaveTaskTypeRequestDTO taskTypeRequestDTO : request.taskTypes()) {
                BigDecimal maxTaskTypeSortIndex = taskTypeRepository.findTopByProjectIdSortBySortIndexDesc(savedProject.getId())
                        .orElse(BigDecimal.valueOf(-1.0));
                BigDecimal newTaskTypeSortIndex = maxTaskTypeSortIndex.add(BigDecimal.valueOf(1.0));

                TaskType taskType = TaskType.builder()
                        .name(taskTypeRequestDTO.name())
                        .project(savedProject)
                        .sortIndex(newTaskTypeSortIndex)
                        .build();
                savedProject.getTaskTypes().add(taskType);
            }
        }

        // Save the project with all relationships
        savedProject = projectRepository.save(savedProject);
        
        // Map to DTO and return
        return projectMapper.projectToProjectResponseDTO(savedProject);
    }

    public ProjectResponseDTO create(UUID folderId, CreateProjectRequestDTO request) {
        String name = request.name().trim();

        User user = authService.getAuthenticatedUser();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + folderId));

        BigDecimal maxSortIndex = projectRepository.findTopByFolderIdSortBySortIndexDesc(folderId)
                .orElse(BigDecimal.valueOf(-1.0)); // Get the highest sort index
        BigDecimal newSortIndex = maxSortIndex.add(BigDecimal.valueOf(1.0));

        Project project = Project.builder()
                .name(request.name())
                .user(user)
                .sortIndex(newSortIndex)
                .folder(folder)
                .build();

        // Save the project first to generate an ID
        Project savedProject = projectRepository.save(project);

        if (!request.taskTypes().isEmpty()) {
            for (SaveTaskStatusRequestDTO taskStatusRequestDTO : request.taskStatuses()) {
                BigDecimal maxTaskStatusSortIndex = taskStatusRepository.findTopByProjectIdSortBySortIndexDesc(savedProject.getId())
                        .orElse(BigDecimal.valueOf(-1.0));
                BigDecimal newTaskStatusSortIndex = maxTaskStatusSortIndex.add(BigDecimal.valueOf(1.0));

                TaskStatus taskStatus = TaskStatus.builder()
                        .name(taskStatusRequestDTO.name())
                        .project(savedProject)
                        .sortIndex(newTaskStatusSortIndex)
                        .build();
                savedProject.getTaskStatuses().add(taskStatus);
            }
        }

        if (!request.taskTypes().isEmpty()) {
            for (SaveTaskTypeRequestDTO taskTypeRequestDTO : request.taskTypes()) {
                BigDecimal maxTaskTypeSortIndex = taskTypeRepository.findTopByProjectIdSortBySortIndexDesc(savedProject.getId())
                        .orElse(BigDecimal.valueOf(-1.0));
                BigDecimal newTaskTypeSortIndex = maxTaskTypeSortIndex.add(BigDecimal.valueOf(1.0));

                TaskType taskType = TaskType.builder()
                        .name(taskTypeRequestDTO.name())
                        .project(savedProject)
                        .sortIndex(newTaskTypeSortIndex)
                        .build();
                savedProject.getTaskTypes().add(taskType);
            }
        }

        // Save the project with all relationships
        savedProject = projectRepository.save(savedProject);

        folder.getProjects().add(savedProject);
        folderRepository.save(folder);

        // Map to DTO and return
        return projectMapper.projectToProjectResponseDTO(savedProject);
    }
}
