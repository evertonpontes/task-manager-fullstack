package com.everton.taskmanager.controllers;

import com.everton.taskmanager.entities.projects.CreateFolderDTO;
import com.everton.taskmanager.entities.projects.Folder;
import com.everton.taskmanager.entities.projects.FolderDTO;
import com.everton.taskmanager.entities.projects.SaveFolderDTO;
import com.everton.taskmanager.entities.teams.*;
import com.everton.taskmanager.services.AttributeService;
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
    public ResponseEntity<FolderDTO> createFolder(@PathVariable("organizationId") String organizationId, @RequestBody @Valid CreateFolderDTO folder) {
        return new ResponseEntity<>(folderService.createFolder(organizationId, folder), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FolderDTO>> findAllFolders(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(folderService.findAllFolders(organizationId), HttpStatus.OK);
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderDTO> findFolder(@PathVariable("folderId") String folderId) {
        return new ResponseEntity<>(folderService.findFolderById(folderId), HttpStatus.OK);
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderDTO> saveFolder(@PathVariable("folderId") String folderId, @RequestBody @Valid SaveFolderDTO folder) {
        return new ResponseEntity<>(folderService.saveFolder(folderId, folder), HttpStatus.OK);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable("folderId") String folderId) {

        folderService.deleteFolder(folderId);

        return new ResponseEntity<String>("Folder deleted successfully.", HttpStatus.OK);
    }
}
