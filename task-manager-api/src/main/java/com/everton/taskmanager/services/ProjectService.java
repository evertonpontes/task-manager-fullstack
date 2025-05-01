package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.GroupMember;
import com.everton.taskmanager.entities.groups.GroupMemberId;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.organizations.OrganizationMember;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.projects.ProjectMember;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.ProjectMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.ProjectRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public GroupResponseDTO createProject(String organizationId, SaveGroupDTO projectDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        String name = projectDTO.name().trim();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a project in this organization.");
        }

        Double sortIndex = projectRepository.findMaxSortIndexByGroupId(organizationId);

        Project project = Project
                .builder()
                .name(name)
                .owner(authenticatedUser)
                .organization(organization)
                .sortIndex(sortIndex)
                .build();

        return projectMapper.projectToGroupResponseDTO(
                projectRepository.save(project)
        );
    }

    public List<GroupResponseDTO> findAllProjectsByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view projects of this organization");
        }

        Set<Project> projects = organization.getProjects();

        return projects
                .stream()
                .map(projectMapper::projectToGroupResponseDTO)
                .toList();
    }

    public GroupResponseDTO findByProjectId(String projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view this project.");
        }

        return projectMapper.projectToGroupResponseDTO(project);
    }

    public GroupResponseDTO updateProject(String projectId, SaveGroupDTO projectDTO) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update this project.");
        }

        String name = projectDTO.name().trim();

        project.setName(name);

        return projectMapper.projectToGroupResponseDTO(projectRepository.save(project));
    }

    public GroupAttributesResponseDTO addAttributesToProject(String projectId,
                                                            SaveAllGroupAttributesDTO attributesDTO) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add attributes in this project.");
        }

        List<Attribute> attributes = attributesDTO.attributes()
                .stream()
                .map(projectMapper::saveAttributeDTOToAttribute)
                .toList();

        attributes.forEach(attribute -> attribute.setProject(project));

        project.getAttributes().clear();
        project.getAttributes().addAll(attributes);

        return projectMapper.projectToGroupAttributesResponseDTO(
                projectRepository.save(project));
    }

    public GroupAttributesResponseDTO findAttributesByProjectId(String projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view attributes in this project.");
        }

        return projectMapper.projectToGroupAttributesResponseDTO(project);
    }

    public List<GroupMemberResponseDTO> addMemberToProject(String projectId, MemberEmailDTO emailDTO) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add a member in this project.");
        }

        Double sortIndex = projectRepository.findMaxSortIndexByGroupId(projectId);

        User member = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        ProjectMember projectMember = new ProjectMember();
        GroupMemberId id = new GroupMemberId(member.getId(), projectId);

        projectMember.setId(id);
        projectMember.setGroup(project);
        projectMember.setMember(member);
        projectMember.setSortIndex(sortIndex + 1);

        project.getMembers().add(projectMember);

        return projectMapper.groupMemberToGroupMemberDTO(projectRepository
                .save(project).getMembers());
    }

    public List<GroupMemberResponseDTO> findMembersByProjectId(String projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view members in this project.");
        }

        return projectMapper.groupMemberToGroupMemberDTO(project.getMembers());
    }

    public void deleteProject(String projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Organization organization = project.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete this project.");
        }

        projectRepository.delete(project);
    }
}
