package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.task.SaveSubTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.CreateTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.TaskResponseDTO;
import com.example.taskmanager.app.dtos.task.UpdateTaskRequestDTO;
import com.example.taskmanager.app.entities.Project;
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
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(CreateTaskRequestDTO request) {
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
            savedTask.setSubTasks(subTasks);
            subTaskRepository.saveAll(subTasks);
        }
        
        return taskMapper.taskToTaskResponseDTO(savedTask);
    }
    
    private SubTask mapToSubTask(Task task, SaveSubTaskRequestDTO dto) {
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

    public List<TaskResponseDTO> getAllTasksForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = currentUser.getTasks();
        return tasks.stream()
                .map(taskMapper::taskToTaskResponseDTO)
                .collect(Collectors.toList());
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
        
        return taskMapper.taskToTaskResponseDTO(task);
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
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
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
            Project project = projectRepository.findById(request.projectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.projectId()));
            task.setProject(project);
        }
        if (request.repeat() != null) {
            task.setRepeat(request.repeat());
        }
        if (request.completed() != null) {
            task.setCompleted(request.completed());
        }
        
        // Save the updated task
        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskResponseDTO(updatedTask);
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
        
        // Create and save the new subtask
        SubTask subTask = SubTask.builder()
                .title(request.title())
                .dueDate(request.dueDate())
                .estimatedTime(request.estimatedTime())
                .parentTask(parentTask)
                .sortIndex(calculateNewSortIndex(parentTask.getId()))
                .build();
        
        // Add subtask to parent's collection and save
        parentTask.getSubTasks().add(subTask);
        Task updatedTask = taskRepository.save(parentTask);
        
        return taskMapper.taskToTaskResponseDTO(updatedTask);
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
        if (request.estimatedTime() != null) {
            subTask.setEstimatedTime(request.estimatedTime());
        }
        
        // Save the parent task to cascade the changes to subtasks
        Task updatedTask = taskRepository.save(parentTask);
        return taskMapper.taskToTaskResponseDTO(updatedTask);
    }
    
    /**
     * Deletes a subtask from the specified parent task.
     * @param taskId ID of the parent task
     * @param subTaskId ID of the subtask to delete
     * @return TaskResponseDTO of the parent task without the deleted subtask
     * @throws EntityNotFoundException if the parent task or subtask is not found
     * @throws UnauthorizedAccessException if the user doesn't have permission to delete the subtask
     */
    @Transactional
    public TaskResponseDTO deleteSubTask(UUID taskId, UUID subTaskId) {
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
        Task updatedTask = taskRepository.save(parentTask);
        return taskMapper.taskToTaskResponseDTO(updatedTask);
    }
    
    /**
     * Calculates the sort index for a new subtask.
     * @param taskId ID of the parent task
     * @return BigDecimal representing the new sort index
     */
    private BigDecimal calculateNewSortIndex(UUID taskId) {
        BigDecimal maxSortIndex = subTaskRepository.findTopByTaskIdSortBySortIndexDesc(taskId)
                .orElse(BigDecimal.valueOf(-1.0));
        return maxSortIndex.add(BigDecimal.ONE);
    }
}
