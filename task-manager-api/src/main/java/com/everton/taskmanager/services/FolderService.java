package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.projects.CreateFolderDTO;
import com.everton.taskmanager.entities.projects.Folder;
import com.everton.taskmanager.entities.projects.FolderDTO;
import com.everton.taskmanager.entities.projects.SaveFolderDTO;
import com.everton.taskmanager.entities.teams.CreateTeamDTO;
import com.everton.taskmanager.entities.teams.SaveTeamDTO;
import com.everton.taskmanager.entities.teams.Team;
import com.everton.taskmanager.entities.teams.TeamDTO;
import com.everton.taskmanager.entities.user.User;
import com.everton.taskmanager.mapper.FolderMapper;
import com.everton.taskmanager.mapper.TeamMapper;
import com.everton.taskmanager.repositories.FolderRepository;
import com.everton.taskmanager.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private FolderMapper folderMapper;

    public FolderDTO createFolder(String organizationId, CreateFolderDTO createFolderDTO) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        Folder newFolder = Folder.builder()
                .name(createFolderDTO.name())
                .organization(organization)
                .build();

        return folderMapper.folderToFolderDTO(folderRepository.save(newFolder));
    }

    public List<FolderDTO> findAllFolders (String organizationId) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        return organization.getFolders().stream()
                .map(folder -> folderMapper.folderToFolderDTO(folder))
                .toList();
    }

    public FolderDTO findFolderById (String folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow();

        return folderMapper.folderToFolderDTO(folder);
    }

    public FolderDTO saveFolder (String folderId, SaveFolderDTO saveFolderDTO) {

        Folder folder = folderRepository.findById(folderId).orElseThrow();

        Organization organization = folder.getOrganization();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!organization.getUser().getId().equals(user.getId())) throw new AccessDeniedException("You do not have permission to edit this resource.");

        folder.setName(saveFolderDTO.name());

        return folderMapper.folderToFolderDTO(folderRepository.save(folder));
    }

    public void deleteFolder (String folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow();

        Organization organization = folder.getOrganization();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!organization.getUser().getId().equals(user.getId())) throw new AccessDeniedException("You do not have permission to delete this resource.");

        try {
            folderRepository.deleteById(folderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
