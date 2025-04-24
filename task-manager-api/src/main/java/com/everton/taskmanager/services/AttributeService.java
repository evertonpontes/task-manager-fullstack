package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.organization.OrganizationAttributesDTO;
import com.everton.taskmanager.entities.organization.SaveOrganizationAttributesDTO;
import com.everton.taskmanager.entities.teams.SaveTeamAttributesDTO;
import com.everton.taskmanager.entities.teams.Team;
import com.everton.taskmanager.entities.teams.TeamAttributesDTO;
import com.everton.taskmanager.entities.user.User;
import com.everton.taskmanager.mapper.OrganizationMapper;
import com.everton.taskmanager.mapper.TeamMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMapper teamMapper;


    public OrganizationAttributesDTO saveOrganizationAttributes(String organizationId, SaveOrganizationAttributesDTO attributesDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        if (!organization.getUser().getId().equals(user.getId())) throw new AccessDeniedException("You do not have permission to edit this resource.");

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

    public TeamAttributesDTO saveTeamAttributes(String teamId, SaveTeamAttributesDTO attributesDTO) {

        Team team = teamRepository.findById(teamId).orElseThrow();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!user.getOwnedOrganizations().contains(team.getOrganization())) throw new AccessDeniedException("You do not have permission to edit this resource.");


        if (attributesDTO.taskTypes() != null && !attributesDTO.taskTypes().isEmpty()) {
            team.getTaskTypes().clear();
            team.getTaskTypes().addAll(organizationMapper.mapCreateAttributeDTOToTaskType(attributesDTO.taskTypes())
                    .stream().peek(taskType -> taskType.setTeam(team)).toList()
            );
        }
        if (attributesDTO.eventTypes() != null && !attributesDTO.eventTypes().isEmpty()) {
            team.getEventTypes().clear();
            team.getEventTypes().addAll(organizationMapper.mapCreateAttributeDTOToEventType(attributesDTO.eventTypes())
                    .stream().peek(eventType -> eventType.setTeam(team)).toList()
            );
        }
        if (attributesDTO.taskStatuses() != null && !attributesDTO.taskStatuses().isEmpty()) {
            team.getTaskStatuses().clear();
            team.getTaskStatuses().addAll(organizationMapper.mapCreateAttributeDTOToTaskStatus(attributesDTO.taskStatuses())
                    .stream().peek(taskStatus -> taskStatus.setTeam(team)).toList()
            );
        }

        return teamMapper.teamToTeamAttributesDTO(teamRepository.save(team));

    }
}
