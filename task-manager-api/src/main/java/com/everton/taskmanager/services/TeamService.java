package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.GroupMember;
import com.everton.taskmanager.entities.groups.GroupMemberId;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.organizations.OrganizationMember;
import com.everton.taskmanager.entities.groups.teams.Team;
import com.everton.taskmanager.entities.groups.teams.TeamMember;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.TeamMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.TeamRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TeamService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public GroupResponseDTO createTeam(String organizationId, SaveGroupDTO teamDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        String name = teamDTO.name().trim();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a team in this organization.");
        }

        Double sortIndex = teamRepository.findMaxSortIndexByGroupId(organizationId);

        Team team = Team
                .builder()
                .name(name)
                .owner(authenticatedUser)
                .organization(organization)
                .sortIndex(sortIndex + 1)
                .build();

        return teamMapper.teamToGroupResponseDTO(
                teamRepository.save(team)
        );
    }

    public List<GroupResponseDTO> findAllTeamsByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view teams of this organization");
        }

        Set<Team> teams = organization.getTeams();

        return teams
                .stream()
                .map(teamMapper::teamToGroupResponseDTO)
                .toList();
    }

    public GroupResponseDTO findByTeamId(String teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view this team.");
        }

        return teamMapper.teamToGroupResponseDTO(team);
    }

    public GroupResponseDTO updateTeam(String teamId, SaveGroupDTO teamDTO) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update this team.");
        }

        String name = teamDTO.name().trim();

        team.setName(name);

        return teamMapper.teamToGroupResponseDTO(teamRepository.save(team));
    }

    public GroupAttributesResponseDTO addAttributesToTeam(String teamId,
                                                                  SaveAllGroupAttributesDTO attributesDTO) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add attributes in this team.");
        }

        List<Attribute> attributes = attributesDTO.attributes()
                .stream()
                .map(teamMapper::saveAttributeDTOToAttribute)
                .toList();

        attributes.forEach(attribute -> attribute.setTeam(team));

        team.getAttributes().clear();
        team.getAttributes().addAll(attributes);

        return teamMapper.teamToGroupAttributesResponseDTO(
                teamRepository.save(team));
    }

    public GroupAttributesResponseDTO findAttributesByTeamId(String teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view attributes in this team.");
        }

        return teamMapper.teamToGroupAttributesResponseDTO(team);
    }

    public List<UserResponseDTO> addMemberToTeam(String teamId, MemberEmailDTO emailDTO) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add a member in this team.");
        }

        Double sortIndex = teamRepository.findMaxSortIndexByGroupId(teamId);

        User member = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        TeamMember teamMember = new TeamMember();
        GroupMemberId id = new GroupMemberId(member.getId(), teamId);

        teamMember.setId(id);
        teamMember.setGroup(team);
        teamMember.setMember(member);
        teamMember.setSortIndex(sortIndex + 1);

        team.getMembers().add(teamMember);

        return teamMapper.userToUserResponseDTO(organizationRepository
                .save(organization).getMembers()
                .stream()
                .map(GroupMember::getMember)
                .toList());
    }

    public List<UserResponseDTO> findMembersByTeamId(String teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view members in this team.");
        }

        return teamMapper.userToUserResponseDTO(organization.getMembers().stream()
                .map(GroupMember::getMember)
                .toList());
    }

    public void deleteTeam(String teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        Organization organization = team.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete this team.");
        }

        teamRepository.delete(team);
    }

}
