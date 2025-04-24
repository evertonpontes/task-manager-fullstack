package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.teams.CreateTeamDTO;
import com.everton.taskmanager.entities.teams.SaveTeamDTO;
import com.everton.taskmanager.entities.teams.Team;
import com.everton.taskmanager.entities.teams.TeamDTO;
import com.everton.taskmanager.entities.user.User;
import com.everton.taskmanager.mapper.TeamMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamMapper teamMapper;

    public TeamDTO createTeam(String organizationId, CreateTeamDTO createTeamDTO) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        Team newTeam = Team.builder()
                .name(createTeamDTO.name())
                .organization(organization)
                .build();

        Team teamWithAttributes = createAttributesTeam(createTeamDTO, newTeam);

        return teamMapper.teamToTeamDTO(teamRepository.save(teamWithAttributes));
    }

    public List<TeamDTO> findAllTeams (String organizationId) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        return organization.getTeams().stream()
                .map(team -> teamMapper.teamToTeamDTO(team))
                .toList();
    }

    public TeamDTO findTeamById (String teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow();

        return teamMapper.teamToTeamDTO(team);
    }

    public TeamDTO saveTeam (String teamId, SaveTeamDTO saveTeamDTO) {

        Team team = teamRepository.findById(teamId).orElseThrow();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!user.getOwnedOrganizations().contains(team.getOrganization())) throw new AccessDeniedException("You do not have permission to edit this resource.");

        team.setName(saveTeamDTO.name());

        return teamMapper.teamToTeamDTO(teamRepository.save(team));
    }

    public void deleteTeam (String teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!user.getOwnedOrganizations().contains(team.getOrganization())) throw new AccessDeniedException("You do not have permission to delete this resource.");

        try {
            teamRepository.deleteById(teamId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Team createAttributesTeam(CreateTeamDTO createTeamDTO, Team team) {

        List<TaskType> taskTypes = teamMapper.mapCreateAttributeDTOToTaskType(createTeamDTO.taskTypes());
        List<EventType> eventTypes = teamMapper.mapCreateAttributeDTOToEventType(createTeamDTO.eventTypes());
        List<TaskStatus> taskStatuses = teamMapper.mapCreateAttributeDTOToTaskStatus(createTeamDTO.taskStatuses());

        taskTypes.forEach(taskType -> taskType.setTeam(team));
        eventTypes.forEach(eventType -> eventType.setTeam(team));
        taskStatuses.forEach(taskStatus -> taskStatus.setTeam(team));

        team.setTaskTypes(taskTypes);
        team.setEventTypes(eventTypes);
        team.setTaskStatuses(taskStatuses);

        return  team;
    }
}
