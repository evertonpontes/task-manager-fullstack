package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.ResourceNotFoundException;
import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.GroupMemberId;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.organizations.OrganizationMember;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.OrganizationMapper;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public GroupResponseDTO createOrganization(SaveGroupDTO organizationDTO) {
        String name = organizationDTO.name().trim();

        User authenticatedUser = authService.getAuthenticatedUser();
        Double sortIndex = organizationRepository.findMaxSortIndexByOwner(authenticatedUser.getId());

        Organization organization = Organization
                .builder()
                .name(name)
                .sortIndex(sortIndex + 1)
                .owner(authenticatedUser)
                .build();

        return organizationMapper.organizationToGroupResponseDTO(
                organizationRepository.save(organization)
        );
    }

    public List<GroupResponseDTO> findAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAllByOwnerId(
                authService.getAuthenticatedUser().getId()
        );

        return organizations
                .stream()
                .map(organizationMapper::organizationToGroupResponseDTO)
                .toList();
    }

    public GroupResponseDTO findByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view this organization.");
        }

        return organizationMapper.organizationToGroupResponseDTO(organization);
    }

    public GroupResponseDTO updateOrganization(String organizationId, SaveGroupDTO organizationDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to update this organization.");
        }

        String name = organizationDTO.name().trim();

        organization.setName(name);

        return organizationMapper.organizationToGroupResponseDTO(
                organizationRepository.save(organization)
        );
    }

    public GroupAttributesResponseDTO addAttributesToOrganization(String organizationId,
                                                                  SaveAllGroupAttributesDTO attributesDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add attributes in this organization.");
        }

        List<Attribute> attributes = attributesDTO.attributes()
                .stream()
                .map(organizationMapper::saveAttributeDTOToAttribute)
                .toList();

        attributes.forEach(attribute -> attribute.setOrganization(organization));

        organization.getAttributes().clear();
        organization.getAttributes().addAll(attributes);

        return organizationMapper.organizationToGroupAttributesResponseDTO(
                organizationRepository.save(organization));
    }

    public GroupAttributesResponseDTO findAttributesByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view attributes in this organization.");
        }

        return organizationMapper.organizationToGroupAttributesResponseDTO(organization);
    }

    public List<GroupMemberResponseDTO> addMemberToOrganization(String organizationId, MemberEmailDTO emailDTO) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to add a member in this organization.");
        }

        Double sortIndex = organizationRepository.findMaxSortIndexByGroupId(organizationId);

        User member = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        OrganizationMember organizationMember = new OrganizationMember();
        GroupMemberId id = new GroupMemberId(member.getId(), organizationId);

        organizationMember.setId(id);
        organizationMember.setGroup(organization);
        organizationMember.setMember(member);
        organizationMember.setSortIndex(sortIndex + 1);

        organization.getMembers().add(organizationMember);

        return organizationMapper.groupMemberToGroupMemberDTO(organizationRepository
                .save(organization).getMembers());
    }

    public List<GroupMemberResponseDTO> findMembersByOrganizationId(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.hasAccess(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to view members in this organization.");
        }

        return organizationMapper.groupMemberToGroupMemberDTO(organization.getMembers());
    }

    public void deleteOrganization(String organizationId) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        User authenticatedUser = authService.getAuthenticatedUser();

        if (!organization.isOwner(authenticatedUser)) {
            throw new AccessDeniedException("You do not have permission to delete this organization.");
        }

        organizationRepository.delete(organization);
    }

}
