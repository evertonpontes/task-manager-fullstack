package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.organization.OrganizationAttributesDTO;
import com.everton.taskmanager.entities.organization.SaveOrganizationAttributesDTO;
import com.everton.taskmanager.mapper.OrganizationMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;


    public OrganizationAttributesDTO saveAttributes(String organizationId, SaveOrganizationAttributesDTO attributesDTO) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        if (attributesDTO.taskTypes() != null && !attributesDTO.taskTypes().isEmpty()) {
            organization.getTaskTypes().clear();
            organization.getTaskTypes().addAll(organizationMapper.mapCreateAttributeDTOToTaskType(attributesDTO.taskTypes())
                    .stream().peek(taskType -> taskType.setOrganization(organization)).toList()
            );
        }
        if (attributesDTO.eventTypes() != null && !attributesDTO.eventTypes().isEmpty()) {
            organization.getEventTypes().clear();
            organization.getEventTypes().addAll(organizationMapper.mapCreateAttributeDTOToEventType(attributesDTO.eventTypes())
                    .stream().peek(eventType -> eventType.setOrganization(organization)).toList()
            );
        }
        if (attributesDTO.taskStatuses() != null && !attributesDTO.taskStatuses().isEmpty()) {
            organization.getTaskStatuses().clear();
            organization.getTaskStatuses().addAll(organizationMapper.mapCreateAttributeDTOToTaskStatus(attributesDTO.taskStatuses())
                    .stream().peek(taskStatus -> taskStatus.setOrganization(organization)).toList()
            );
        }

        return organizationMapper.organizationToOrganizationAttributesDTO(organizationRepository.save(organization));

    }
}
