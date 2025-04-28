package com.everton.taskmanager.mapper;

import com.everton.taskmanager.dtos.attribute.AttributeResponseDTO;
import com.everton.taskmanager.dtos.attribute.SaveAttributeDTO;
import com.everton.taskmanager.dtos.groups.GroupAttributesResponseDTO;
import com.everton.taskmanager.dtos.groups.GroupResponseDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.attributes.AttributeTypeEnum;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    public GroupResponseDTO projectToGroupResponseDTO(Project project);

    public Attribute saveAttributeDTOToAttribute(SaveAttributeDTO attributeDTO);

    @Mapping(target = "taskTypes", source = "attributes", qualifiedByName = "mapTaskType")
    @Mapping(target = "eventTypes", source = "attributes", qualifiedByName = "mapEventType")
    @Mapping(target = "taskStatuses", source = "attributes", qualifiedByName = "mapTaskStatus")
    public GroupAttributesResponseDTO projectToGroupAttributesResponseDTO(Project project);

    default AttributeResponseDTO attributeToAttributesResponseDTO(Attribute attribute) {
        return new AttributeResponseDTO(
                attribute.getId(),
                attribute.getName(),
                attribute.getColor()
        );
    }

    @Named("mapTaskType")
    default List<AttributeResponseDTO> mapTaskTypes(List<Attribute> attributes) {
        return attributes.stream()
                .filter(attribute -> AttributeTypeEnum.TASK_TYPE.equals(attribute.getType()))
                .map(this::attributeToAttributesResponseDTO)
                .toList();
    }

    @Named("mapEventType")
    default List<AttributeResponseDTO> mapEventTypes(List<Attribute> attributes) {
        return attributes.stream()
                .filter(attribute -> AttributeTypeEnum.EVENT_TYPE.equals(attribute.getType()))
                .map(this::attributeToAttributesResponseDTO)
                .toList();
    }

    @Named("mapTaskStatus")
    default List<AttributeResponseDTO> mapTaskStatuses(List<Attribute> attributes) {
        return attributes.stream()
                .filter(attribute -> AttributeTypeEnum.TASK_STATUS.equals(attribute.getType()))
                .map(this::attributeToAttributesResponseDTO)
                .toList();
    }

    public List<UserResponseDTO> userToUserResponseDTO(List<User> users);
}
