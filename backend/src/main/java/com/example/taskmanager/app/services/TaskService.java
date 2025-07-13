package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.task.SaveSubTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.CreateTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.TaskResponseDTO;
import com.example.taskmanager.app.dtos.task.UpdateTaskRequestDTO;
import com.example.taskmanager.app.entities.Node;
import com.example.taskmanager.app.entities.TaskStatus;
import com.example.taskmanager.app.entities.TaskType;
import com.example.taskmanager.app.entities.task.Task;
import com.example.taskmanager.app.entities.task.SubTask;
import com.example.taskmanager.app.exceptions.UnauthorizedAccessException;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final SubTaskRepository subTaskRepository;
    private final NodeRepository nodeRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(UUID projectId,CreateTaskRequestDTO request) {
        // Get authenticated user
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find task type and status
        TaskType taskType = taskTypeRepository.findById(request.taskTypeId())
                .orElseThrow(() -> new EntityNotFoundException("TaskType not found with id: " + request.taskTypeId()));
                
        TaskStatus taskStatus = taskStatusRepository.findById(request.taskStatusId())
                .orElseThrow(() -> new EntityNotFoundException("TaskStatus not found with id: " + request.taskStatusId()));

        Node project = nodeRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + projectId));

        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new com.example.taskmanager.utils.exceptions.UnauthorizedAccessException("You don't have permission to create a project in this folder");
        }
        
        BigDecimal commonRank = BigDecimal.valueOf(System.currentTimeMillis());

        // Create main task
        Task task = Task.builder()
                .title(request.title())
                .commonRank(commonRank)
                .statusRank(taskStatus.getOrderIndex())
                .description(request.description())
                .priority(request.priority())
                .startAt(request.startAt())
                .estimatedTime(request.estimatedTime())
                .spentTime(request.spentTime())
                .dueDate(request.dueDate())
                .taskType(taskType)
                .taskStatus(taskStatus)
                .user(currentUser)
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
            savedTask.setSubTasks(subTasks);
            subTaskRepository.saveAll(subTasks);
        }
        
        return taskMapper.toTaskResponseDTO(savedTask);
    }
    
    private SubTask mapToSubTask(Task task, SaveSubTaskRequestDTO dto) {
        Long maxRank = subTaskRepository.findMaxRankByTaskId(task.getId()).orElse(0L);
        Long newRank = maxRank + 10000L;

        return SubTask.builder()
                .title(dto.title())
                .rank(newRank)
                .estimatedTime(dto.estimatedTime())
                .status(dto.status())
                .dueDate(dto.dueDate())// Default sort index
                .parentTask(task)
                .build();
    }

    public List<TaskResponseDTO> getAllTasksForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = currentUser.getTasks();
        return null;
    }

    /**
     * Retrieves a task by ID if the current user has permission to access it.
     * @param taskId ID of the task to retrieve
     * @return TaskResponseDTO containing the task details
     * @throws jakarta.persistence.EntityNotFoundException if the task is not found
     * @throws UnauthorizedAccessException if the current user doesn't have permission to access the task
     */
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(UUID taskId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        
        // Check if the current user is the owner of the task
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this task");
        }
        
        return taskMapper.toTaskResponseDTO(task);
    }

    /**
     * Updates an existing task with the provided data.
     * @param taskId ID of the task to update
     * @param request DTO containing the updated task data
     * @return Updated task as TaskResponseDTO
     * @throws EntityNotFoundException if the task is not found
     * @throws UnauthorizedAccessException if the current user doesn't have permission to update the task
     */
    @Transactional
    public TaskResponseDTO updateTask(UUID taskId, UpdateTaskRequestDTO request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find existing task and verify ownership
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update this task");
        }
        
        // Update task fields if they are not null in the request
        if (request.commonRank() != null) {
            task.setCommonRank(request.commonRank());
        }
        if (request.statusRank() != null) {
            task.setStatusRank(request.statusRank());
        }
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.startAt() != null) {
            task.setStartAt(request.startAt());
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }
        if (request.spentTime() != null) {
            task.setSpentTime(request.spentTime());
        }
        if (request.estimatedTime() != null) {
            task.setEstimatedTime(request.estimatedTime());
        }
        if (request.taskTypeId() != null) {
            TaskType taskType = taskTypeRepository.findById(request.taskTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("TaskType not found with id: " + request.taskTypeId()));
            task.setTaskType(taskType);
        }
        if (request.taskStatusId() != null) {
            TaskStatus taskStatus = taskStatusRepository.findById(request.taskStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("TaskStatus not found with id: " + request.taskStatusId()));
            task.setTaskStatus(taskStatus);
        }
        if (request.projectId() != null) {
            Node project = nodeRepository.findById(request.projectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.projectId()));
            task.setProject(project);
        }
        
        // Save the updated task
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(updatedTask);
    }

    /**
     * Deletes a task by ID if the current user has permission.
     * @param taskId ID of the task to delete
     * @throws EntityNotFoundException if the task is not found
     * @throws UnauthorizedAccessException if the current user doesn't have permission to delete the task
     */
    @Transactional
    public void deleteTask(UUID taskId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete this task");
        }
        
        // Cascade delete will handle related entities (subtasks, etc.)
        taskRepository.delete(task);
    }
    
    /**
     * Creates a new subtask for the specified parent task.
     * @param taskId ID of the parent task
     * @param request DTO containing subtask details
     * @return TaskResponseDTO of the parent task with the new subtask
     * @throws EntityNotFoundException if the parent task is not found
     * @throws UnauthorizedAccessException if the user doesn't have permission to add subtasks
     */
    @Transactional
    public TaskResponseDTO createSubTask(UUID taskId, SaveSubTaskRequestDTO request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find parent task and verify ownership
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + taskId));
        
        if (!parentTask.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to add subtasks to this task");
        }

        Long maxRank = subTaskRepository.findMaxRankByTaskId(parentTask.getId()).orElse(0L);
        Long newRank = maxRank + 10000L;
        
        // Create and save the new subtask
        SubTask subTask = SubTask.builder()
                .title(request.title())
                .status(request.status())
                .estimatedTime(request.estimatedTime())
                .rank(newRank)
                .dueDate(request.dueDate())
                .parentTask(parentTask)
                .build();
        
        // Add subtask to parent's collection and save
        parentTask.getSubTasks().add(subTask);
        Task updatedTask = taskRepository.save(parentTask);
        
        return taskMapper.toTaskResponseDTO(updatedTask);
    }
    
    /**
     * Updates an existing subtask.
     * @param taskId ID of the parent task
     * @param subTaskId ID of the subtask to update
     * @param request DTO containing updated subtask details
     * @return TaskResponseDTO of the parent task with the updated subtask
     * @throws EntityNotFoundException if the parent task or subtask is not found
     * @throws UnauthorizedAccessException if the user doesn't have permission to update the subtask
     */
    @Transactional
    public TaskResponseDTO updateSubTask(UUID taskId, UUID subTaskId, SaveSubTaskRequestDTO request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find parent task and verify ownership
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + taskId));
        
        if (!parentTask.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update subtasks in this task");
        }
        
        // Find the subtask to update
        SubTask subTask = parentTask.getSubTasks().stream()
                .filter(st -> st.getId().equals(subTaskId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Subtask not found with id: " + subTaskId));
        
        // Update subtask fields
        if (request.title() != null) {
            subTask.setTitle(request.title());
        }
        if (request.dueDate() != null) {
            subTask.setDueDate(request.dueDate());
        }
        if (request.status() != null) {
            subTask.setStatus(request.status());
        }
        if (request.estimatedTime() != null) {
            subTask.setEstimatedTime(request.estimatedTime());
        }
        
        // Save the parent task to cascade the changes to subtasks
        Task updatedTask = taskRepository.save(parentTask);
        return taskMapper.toTaskResponseDTO(updatedTask);
    }
    
    /**
     * Deletes a subtask from the specified parent task.
     * @param taskId ID of the parent task
     * @param subTaskId ID of the subtask to delete
     * @throws EntityNotFoundException if the parent task or subtask is not found
     * @throws UnauthorizedAccessException if the user doesn't have permission to delete the subtask
     */
    @Transactional
    public void deleteSubTask(UUID taskId, UUID subTaskId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Find parent task and verify ownership
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + taskId));
        
        if (!parentTask.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete subtasks from this task");
        }
        
        // Find and remove the subtask
        boolean removed = parentTask.getSubTasks().removeIf(
            subTask -> subTask.getId().equals(subTaskId)
        );
        
        if (!removed) {
            throw new EntityNotFoundException("Subtask not found with id: " + subTaskId);
        }
        
        // Save the parent task to cascade the deletion of the subtask
        taskRepository.save(parentTask);
    }
}
