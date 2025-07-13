package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.attributes.TaskStatusResponseDTO;
import com.example.taskmanager.app.dtos.attributes.TaskTypeResponseDTO;
import com.example.taskmanager.app.dtos.task.SubTaskResponseDTO;
import com.example.taskmanager.app.dtos.task.TaskResponseDTO;
import com.example.taskmanager.app.entities.TaskStatus;
import com.example.taskmanager.app.entities.TaskType;
import com.example.taskmanager.app.entities.task.SubTask;
import com.example.taskmanager.app.entities.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "subTasks", expression = "java(task.getSubTasks() == null ? null : task.getSubTasks().stream().map(this::mapToSubTaskResponseDTO).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "taskType", source = "taskType")
    @Mapping(target = "taskStatus", source = "taskStatus")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "projectId", source = "project.id")
    TaskResponseDTO toTaskResponseDTO(Task task);

    @Mapping(target = "parentTaskId", source = "parentTask.id")
    SubTaskResponseDTO mapToSubTaskResponseDTO(SubTask subTask);

    TaskTypeResponseDTO mapToTaskTypeResponseDTO(TaskType taskType);

    TaskStatusResponseDTO mapToTaskStatusResponseDTO(TaskStatus taskStatus);
}
