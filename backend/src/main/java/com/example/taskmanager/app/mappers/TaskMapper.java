package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.task.TaskResponseDTO;
import com.example.taskmanager.app.entities.task.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponseDTO taskToTaskResponseDTO(Task task);
}
