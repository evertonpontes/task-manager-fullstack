package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.task.CreateSubTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.CreateTaskRequestDTO;
import com.example.taskmanager.app.entities.Project;
import com.example.taskmanager.app.entities.TaskStatus;
import com.example.taskmanager.app.entities.TaskType;
import com.example.taskmanager.app.entities.task.Task;
import com.example.taskmanager.app.entities.task.SubTask;
import com.example.taskmanager.app.mappers.TaskMapper;
import com.example.taskmanager.app.repositories.*;
import com.example.taskmanager.user.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final SubTaskRepository subTaskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Task createTask(CreateTaskRequestDTO request) {
        // Get authenticated user
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find task type and status
        TaskType taskType = taskTypeRepository.findById(request.taskTypeId())
                .orElseThrow(() -> new EntityNotFoundException("TaskType not found with id: " + request.taskTypeId()));
                
        TaskStatus taskStatus = taskStatusRepository.findById(request.taskStatusId())
                .orElseThrow(() -> new EntityNotFoundException("TaskStatus not found with id: " + request.taskStatusId()));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.projectId()));

        BigDecimal maxSortIndex = taskRepository.findTopByProjectIdSortBySortIndexDesc(project.getId()).orElse(BigDecimal.valueOf(-1.0));
        BigDecimal newSortIndex = maxSortIndex.add(BigDecimal.valueOf(1.0));

        // Create main task
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .estimatedTime(request.estimatedTime())
                .taskType(taskType)
                .taskStatus(taskStatus)
                .user(currentUser)
                .sortIndex(newSortIndex)
                .project(project)
                .build();
        
        // Save the main task first to generate an ID
        Task savedTask = taskRepository.save(task);
        
        // Handle subtasks if present
        if (request.subTasks() != null && !request.subTasks().isEmpty()) {
            List<SubTask> subTasks = request.subTasks().stream()
                    .map(subTask -> mapToSubTask(savedTask, subTask))
                    .peek(subTask -> subTask.setParentTask(savedTask))
                    .collect(Collectors.toList());
            
            savedTask.setSubTasks(subTasks);
            // Save again to persist the subtasks
            return taskRepository.save(savedTask);
        }
        
        return savedTask;
    }
    
    private SubTask mapToSubTask(Task task,CreateSubTaskRequestDTO dto) {
        BigDecimal maxSortIndex = subTaskRepository.findTopByTaskIdSortBySortIndexDesc(task.getId()).orElse(BigDecimal.valueOf(-1.0));
        BigDecimal newSortIndex = maxSortIndex.add(BigDecimal.valueOf(1.0));

        return SubTask.builder()
                .title(dto.title())
                .dueDate(dto.dueDate())
                .estimatedTime(dto.estimatedTime())
                .completed(false)
                .sortIndex(newSortIndex) // Default sort index
                .build();
    }
}
