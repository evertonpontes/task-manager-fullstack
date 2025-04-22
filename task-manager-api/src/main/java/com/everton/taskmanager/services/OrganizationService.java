package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.organization.OrgResponseDTO;
import com.everton.taskmanager.entities.organization.OrgToCreateDTO;
import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.user.User;
import com.everton.taskmanager.exceptions.AlreadyExistsException;
import com.everton.taskmanager.exceptions.UserNotFoundException;
import com.everton.taskmanager.repositories.OrganizationRepository;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    public OrgResponseDTO createOrganization(String userId, OrgToCreateDTO orgToCreateDTO) {

        User user = userRepository.findById(userId).orElseThrow();

        if (organizationRepository.findOrganizationByUserIdAndName(user.getId(), orgToCreateDTO.name()) != null) throw new AlreadyExistsException("This name was already given to another organization.");

        Organization newOrganization = Organization
                .builder()
                .name(orgToCreateDTO.name())
                .user(user)
                .build();

        Organization organizationSaved = organizationRepository.save(newOrganization);

        return OrgResponseDTO
                .builder()
                .name(organizationSaved.getName())
                .owner_email(user.getEmail())
                .build();
    }
}
