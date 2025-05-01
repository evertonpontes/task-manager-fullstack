package com.everton.taskmanager.mapper;

import com.everton.taskmanager.dtos.attribute.AttributeResponseDTO;
import com.everton.taskmanager.dtos.attribute.SaveAttributeDTO;
import com.everton.taskmanager.dtos.groups.GroupAttributesResponseDTO;
import com.everton.taskmanager.dtos.groups.GroupMemberResponseDTO;
import com.everton.taskmanager.dtos.groups.GroupResponseDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.attributes.AttributeTypeEnum;
import com.everton.taskmanager.entities.groups.GroupMemberId;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.organizations.OrganizationMember;
import com.everton.taskmanager.entities.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    public GroupResponseDTO organizationToGroupResponseDTO(Organization organization);

    public Attribute saveAttributeDTOToAttribute(SaveAttributeDTO attributeDTO);

    @Mapping(target = "taskTypes", source = "attributes", qualifiedByName = "mapTaskType")
    @Mapping(target = "eventTypes", source = "attributes", qualifiedByName = "mapEventType")
    @Mapping(target = "taskStatuses", source = "attributes", qualifiedByName = "mapTaskStatus")
    public GroupAttributesResponseDTO organizationToGroupAttributesResponseDTO(Organization organization);

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

    public List<GroupMemberResponseDTO> groupMemberToGroupMemberDTO(Set<OrganizationMember> groupMembers);

    @Mapping(target = "memberDetails", source = "member", qualifiedByName = "userToUserResponseDTO")
    @Mapping(target = "id", source = "id", qualifiedByName = "mapGroupMemberId")
    GroupMemberResponseDTO groupMemberToDTO(OrganizationMember groupMember);

    @Named("userToUserResponseDTO")
    public UserResponseDTO userToUserResponseDTO(User user);

    @Named("mapGroupMemberId")
    default String mapGroupMemberId(GroupMemberId value) {
        if (value == null) return null;
        return value.getMemberId();
    }
}