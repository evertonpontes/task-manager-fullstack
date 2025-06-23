package com.example.taskmanager.app.controllers;

import com.example.taskmanager.app.dtos.folder.FolderResponseDTO;
import com.example.taskmanager.app.dtos.folder.SaveFolderRequestDTO;
import com.example.taskmanager.app.dtos.project.CreateProjectRequestDTO;
import com.example.taskmanager.app.dtos.project.ProjectResponseDTO;
import com.example.taskmanager.app.services.FolderService;
import com.example.taskmanager.app.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<FolderResponseDTO> createFolder(@Valid @RequestBody SaveFolderRequestDTO request) {
        return new ResponseEntity<>(folderService.createFolder(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@PathVariable UUID folderId, @Valid @RequestBody CreateProjectRequestDTO request) {
        return new ResponseEntity<>(projectService.create(folderId, request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FolderResponseDTO>> getAllUserFolders() {
        return new ResponseEntity<>(folderService.getAllUserFolders(), HttpStatus.OK);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsByFolderId(@PathVariable UUID id) {
        return new ResponseEntity<>(projectService.getAllProjectsByFolderId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponseDTO> getFolderById(@PathVariable UUID id) {
        return new ResponseEntity<>(folderService.getFolderById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolderResponseDTO> updateFolder(
            @PathVariable UUID id,
            @Valid @RequestBody SaveFolderRequestDTO request) {
        return new ResponseEntity<>(folderService.updateFolderName(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable UUID id) {
        folderService.deleteFolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
