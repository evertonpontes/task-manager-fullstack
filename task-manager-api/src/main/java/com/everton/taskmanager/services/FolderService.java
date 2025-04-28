package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.FolderMapper;
import com.everton.taskmanager.mapper.ProjectMapper;
import com.everton.taskmanager.repositories.FolderRepository;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FolderService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public GroupResponseDTO createFolder(String organizationId, SaveGroupDTO folderDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        String name = folderDTO.name().trim();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a folder in this organization.");
        }

        Folder folder = Folder
                .builder()
                .name(name)
                .owner(authenticatedUser)
                .organization(organization)
                .build();

        return folderMapper.folderToGroupResponseDTO(
                folderRepository.save(folder)
        );
    }

    public List<GroupResponseDTO> findAllFoldersByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view folders of this organization");
        }

        Set<Folder> folders = organization.getFolders();

        return folders
                .stream()
                .map(folderMapper::folderToGroupResponseDTO)
                .toList();
    }

    public GroupResponseDTO findByFolderId(String folderId) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view this folder.");
        }

        return folderMapper.folderToGroupResponseDTO(folder);
    }

    public GroupResponseDTO updateFolder(String folderId, SaveGroupDTO folderDTO) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update this folder.");
        }

        String name = folderDTO.name().trim();

        folder.setName(name);

        return folderMapper.folderToGroupResponseDTO(folderRepository.save(folder));
    }

    public List<GroupResponseDTO> addProjectToFolder (String folderId, SaveGroupDTO projectDTO) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to create a project in this folder.");
        }

        String name = projectDTO.name().trim();

        Project project = Project.builder()
                .name(name)
                .folder(folder)
                .organization(organization)
                .owner(authenticatedUser)
                .build();

        folder.getProjects().add(project);
        folderRepository.save(folder);

        return folder.getProjects()
                .stream()
                .map(projectMapper::projectToGroupResponseDTO)
                .toList();

    }

    public List<GroupResponseDTO> findProjectsByFolderId (String folderId) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view projects of this folder.");
        }

        return folder.getProjects()
                .stream()
                .map(projectMapper::projectToGroupResponseDTO)
                .toList();
    }

    public GroupAttributesResponseDTO addAttributesToFolder(String folderId,
                                                          SaveAllGroupAttributesDTO attributesDTO) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add attributes in this folder.");
        }

        List<Attribute> attributes = attributesDTO.attributes()
                .stream()
                .map(folderMapper::saveAttributeDTOToAttribute)
                .toList();

        attributes.forEach(attribute -> attribute.setFolder(folder));

        folder.getAttributes().clear();
        folder.getAttributes().addAll(attributes);

        return folderMapper.folderToGroupAttributesResponseDTO(
                folderRepository.save(folder));
    }

    public GroupAttributesResponseDTO findAttributesByFolderId(String folderId) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view attributes in this folder.");
        }

        return folderMapper.folderToGroupAttributesResponseDTO(folder);
    }

    public List<UserResponseDTO> addMemberToFolder(String folderId, MemberEmailDTO emailDTO) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add a member in this folder.");
        }

        User member = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        organization.getMembers().add(member);

        return folderMapper.userToUserResponseDTO(organizationRepository
                .save(organization).getMembers().stream().toList());
    }

    public List<UserResponseDTO> findMembersByFolderId(String folderId) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view members in this folder.");
        }

        return folderMapper.userToUserResponseDTO(organization.getMembers().stream().toList());
    }

    public void deleteFolder(String folderId) {
        Folder folder = folderRepository
                .findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found."));

        Organization organization = folder.getOrganization();

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete this folder.");
        }

        folderRepository.delete(folder);
    }

}
