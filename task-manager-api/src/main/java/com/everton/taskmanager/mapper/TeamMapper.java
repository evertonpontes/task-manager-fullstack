package com.everton.taskmanager.mapper;

import com.everton.taskmanager.entities.attributes.AttributeDTO;
import com.everton.taskmanager.entities.attributes.CreateAttributeDTO;
import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.teams.Team;
import com.everton.taskmanager.entities.teams.TeamAttributesDTO;
import com.everton.taskmanager.entities.teams.TeamDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "taskTypes", qualifiedByName = "mapTaskTypeToAttributeDTO")
    @Mapping(target = "eventTypes", qualifiedByName = "mapEventTypeToAttributeDTO")
    @Mapping(target = "taskStatuses", qualifiedByName = "mapTaskStatusToAttributeDTO")
    TeamDTO teamToTeamDTO(Team team);

    @Mapping(target = "taskTypes", qualifiedByName = "mapTaskTypeToAttributeDTO")
    @Mapping(target = "eventTypes", qualifiedByName = "mapEventTypeToAttributeDTO")
    @Mapping(target = "taskStatuses", qualifiedByName = "mapTaskStatusToAttributeDTO")
    TeamAttributesDTO teamToTeamAttributesDTO (Team team);

    // ------------- TASK TYPE ------------------

    @Named("mapTaskTypeToAttributeDTO")
    @IterableMapping(qualifiedByName = "mapTaskTypeToAttributeDTO")
    List<AttributeDTO> mapTaskTypeToAttributeDTO (List<TaskType> taskTypes);

    @Named("mapTaskTypeToAttributeDTO")
    AttributeDTO mapTaskTypeToAttributeDTO (TaskType taskType);

    // -------------- EVENT TYPE -----------------

    @Named("mapEventTypeToAttributeDTO")
    @IterableMapping(qualifiedByName = "mapEventTypeToAttributeDTO")
    List<AttributeDTO> mapEventTypeToAttributeDTO (List<EventType> eventTypes);

    @Named("mapEventTypeToAttributeDTO")
    AttributeDTO mapEventTypeToAttributeDTO (EventType eventType);

    // ------------- TASK STATUS ----------------

    @Named("mapTaskStatusToAttributeDTO")
    @IterableMapping(qualifiedByName = "mapTaskStatusToAttributeDTO")
    List<AttributeDTO> mapTaskStatusToAttributeDTO (List<TaskStatus> taskStatuses);

    @Named("mapTaskStatusToAttributeDTO")
    AttributeDTO mapTaskStatusToAttributeDTO (TaskStatus taskStatus);

    // -------------------------------------------

    @IterableMapping(qualifiedByName = "mapCreateAttributeDTOToTaskType")
    List<TaskType> mapCreateAttributeDTOToTaskType (List<CreateAttributeDTO> createAttributeDTOS);

    @Named("mapCreateAttributeDTOToTaskType")
    TaskType mapCreateAttributeDTOToTaskType(CreateAttributeDTO createAttributeDTO);

    @IterableMapping(qualifiedByName = "mapCreateAttributeDTOToEventType")
    List<EventType> mapCreateAttributeDTOToEventType (List<CreateAttributeDTO> createAttributeDTOS);

    @Named("mapCreateAttributeDTOToEventType")
    EventType mapCreateAttributeDTOToEventType(CreateAttributeDTO createAttributeDTO);

    @IterableMapping(qualifiedByName = "mapCreateAttributeDTOToTaskStatus")
    List<TaskStatus> mapCreateAttributeDTOToTaskStatus (List<CreateAttributeDTO> createAttributeDTOS);

    @Named("mapCreateAttributeDTOToTaskStatus")
    TaskStatus mapCreateAttributeDTOToTaskStatus(CreateAttributeDTO createAttributeDTO);
}
