package com.everton.taskmanager.mapper;

import com.everton.taskmanager.dtos.task.*;
import com.everton.taskmanager.entities.tasks.SubTask;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.tasks.tasktiming.LoggedTime;
import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import com.everton.taskmanager.entities.users.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    public TaskResponseDTO taskToTaskResponseDTO(Task task);

    public Task saveTaskDTOToTask(SaveTaskDTO taskDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateTaskFromDTO(SaveTaskDTO taskDTO, @MappingTarget Task task);

    public SubTask saveSubTaskDTOToSubTask(SaveSubTaskDTO subTaskDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateSubTaskFromDTO(SaveSubTaskDTO subTaskDTO, @MappingTarget SubTask subTask);

    public SubTaskResponseDTO subTaskToSubTaskResponseDTO(SubTask subTask);

    public ScheduledWork saveScheduledWorksDTOToScheduledWorks(SaveScheduledWorksDTO scheduledWorksDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateScheduledWorksFromDTO(SaveScheduledWorksDTO scheduledWorksDTO,
                                            @MappingTarget ScheduledWork scheduledWork);

    @Mapping(target = "assigneeEmail", source = "assignee", qualifiedByName = "getAssigneeEmail")
    public ScheduledWorkResponseDTO scheduledWorkToScheduledWorkResponseDTO(ScheduledWork scheduledWork);

    @Named("getAssigneeEmail")
    default String getAssigneeEmail(User assignee) {
        if (assignee == null) return null;
        return assignee.getEmail();
    }

    public LoggedTime saveLoggedTimeDTOToLoggedTime(SaveLoggedTimeDTO loggedTimeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateLoggedTimeFromDTO(SaveLoggedTimeDTO loggedTimeDTO,
                                            @MappingTarget LoggedTime loggedTime);

    @Mapping(target = "assigneeEmail", source = "assignee", qualifiedByName = "getAssigneeEmail")
    public LoggedTimeResponseDTO loggedTimeToLoggedTimeResponseDTO(LoggedTime loggedTime);

}
