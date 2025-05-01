package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.services.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations/{organizationId}/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createFolder(@PathVariable("organizationId") String organizationId,
                                                         @RequestBody @Valid SaveGroupDTO folderDTO) {
        return new ResponseEntity<>(folderService.createFolder(organizationId, folderDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getAllFolderByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return  new ResponseEntity<>(folderService.findAllFoldersByOrganizationId(organizationId), HttpStatus.OK);
    }

    @GetMapping("{folderId}")
    public ResponseEntity<GroupResponseDTO> getFolder(@PathVariable("folderId") String folderId) {
        return  new ResponseEntity<>(folderService.findByFolderId(folderId), HttpStatus.OK);
    }

    @PutMapping("{folderId}")
    public ResponseEntity<GroupResponseDTO> updateFolder(@PathVariable("folderId") String folderId,
                                                          @RequestBody @Valid SaveGroupDTO folderDTO) {
        return new ResponseEntity<>(folderService.updateFolder(folderId, folderDTO), HttpStatus.OK);
    }

    @PostMapping("{folderId}/projects")
    public ResponseEntity<List<GroupResponseDTO>> addProjectToFolder(@PathVariable("folderId") String folderId,
                                                                       @RequestBody @Valid SaveGroupDTO projectDTO) {
        return  new ResponseEntity<>(folderService.addProjectToFolder(folderId, projectDTO), HttpStatus.CREATED);
    }

    @GetMapping("{folderId}/projects")
    public ResponseEntity<List<GroupResponseDTO>> getProjectsByFolder(@PathVariable("folderId") String folderId) {
        return  new ResponseEntity<>(folderService.findProjectsByFolderId(folderId), HttpStatus.OK);
    }

    @PostMapping("{folderId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> addAttributesToFolder(@PathVariable("folderId") String folderId,
                                                                                  @RequestBody @Valid SaveAllGroupAttributesDTO attributesDTO) {
        return new ResponseEntity<>(folderService.addAttributesToFolder(folderId, attributesDTO), HttpStatus.CREATED);
    }

    @GetMapping("{folderId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> getAttributesByFolder(@PathVariable("folderId") String folderId) {
        return new ResponseEntity<>(folderService.findAttributesByFolderId(folderId), HttpStatus.OK);
    }

    @DeleteMapping("{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable("folderId") String folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }
}
