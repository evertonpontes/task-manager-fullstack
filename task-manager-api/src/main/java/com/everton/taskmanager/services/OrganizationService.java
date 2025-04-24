package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.organization.CreateOrganizationDTO;
import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.organization.OrganizationDTO;
import com.everton.taskmanager.entities.organization.SaveOrganizationDTO;
import com.everton.taskmanager.entities.user.User;
import com.everton.taskmanager.exceptions.AlreadyExistsException;
import com.everton.taskmanager.mapper.OrganizationMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    public OrganizationDTO createOrganization(CreateOrganizationDTO organization) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName());

        if (organizationRepository.findOrganizationByUserIdAndName(user.getId(), organization.name()) != null) throw new AlreadyExistsException("This name was already given to another organization.");

        Organization newOrganization = Organization
                .builder()
                .name(organization.name())
                .user(user)
                .build();

        Organization organizationWithAttributes = createAttributesOrganization(organization, newOrganization);

        return organizationMapper.organizationToOrganizationDTO(organizationRepository.save(organizationWithAttributes));
    }

    public List<OrganizationDTO> findAllOrganizations () {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName());

        List<Organization> organizations = organizationRepository.findAllOrganizationsByUserId(user.getId());

        return organizations.stream()
                .map(organization -> organizationMapper.organizationToOrganizationDTO(organization))
                .toList();
    }

    public OrganizationDTO findOrganization (String organizationId) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        return organizationMapper.organizationToOrganizationDTO(organizationRepository.save(organization));
    }

    public OrganizationDTO saveOrganization (String organizationId, SaveOrganizationDTO organizationDTO) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        organization.setName(organizationDTO.name());

        return organizationMapper.organizationToOrganizationDTO(organizationRepository.save(organization));
    }

    public void deleteOrganization (String organizationId) {
        try {
            organizationRepository.deleteById(organizationId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Organization createAttributesOrganization(CreateOrganizationDTO organizationDTO, Organization organization) {

        List<TaskType> taskTypes = organizationMapper.mapCreateAttributeDTOToTaskType(organizationDTO.taskTypes());
        List<EventType> eventTypes = organizationMapper.mapCreateAttributeDTOToEventType(organizationDTO.eventTypes());
        List<TaskStatus> taskStatuses = organizationMapper.mapCreateAttributeDTOToTaskStatus(organizationDTO.taskStatuses());

        taskTypes.forEach(taskType -> taskType.setOrganization(organization));
        eventTypes.forEach(eventType -> eventType.setOrganization(organization));
        taskStatuses.forEach(taskStatus -> taskStatus.setOrganization(organization));

        organization.setTaskTypes(taskTypes);
        organization.setEventTypes(eventTypes);
        organization.setTaskStatuses(taskStatuses);

        return  organization;
    }
}
