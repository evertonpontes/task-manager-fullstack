package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations/{organizationId}/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createProject(@PathVariable("organizationId") String organizationId,
                                                          @RequestBody @Valid SaveGroupDTO projectDTO) {
        return new ResponseEntity<>(projectService.createProject(organizationId, projectDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getAllProjectsByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return  new ResponseEntity<>(projectService.findAllProjectsByOrganizationId(organizationId), HttpStatus.OK);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<GroupResponseDTO> getProject(@PathVariable("projectId") String projectId) {
        return  new ResponseEntity<>(projectService.findByProjectId(projectId), HttpStatus.OK);
    }

    @PutMapping("{projectId}")
    public ResponseEntity<GroupResponseDTO> updateProject(@PathVariable("projectId") String projectId,
                                                      @RequestBody @Valid SaveGroupDTO projectDTO) {
        return new ResponseEntity<>(projectService.updateProject(projectId, projectDTO), HttpStatus.OK);
    }

    @PostMapping("{projectId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> addAttributesToProject(@PathVariable("projectId") String projectId,
                                                                                  @RequestBody @Valid SaveAllGroupAttributesDTO attributesDTO) {
        return new ResponseEntity<>(projectService.addAttributesToProject(projectId, attributesDTO), HttpStatus.CREATED);
    }

    @GetMapping("{projectId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> getAttributesByProject(@PathVariable("projectId") String projectId) {
        return new ResponseEntity<>(projectService.findAttributesByProjectId(projectId), HttpStatus.OK);
    }

    @PostMapping("{projectId}/members")
    public ResponseEntity<List<UserResponseDTO>> addMemberToProject(@PathVariable("projectId") String projectId,
                                                                    @RequestBody @Valid MemberEmailDTO emailDTO) {
        return new ResponseEntity<>(projectService.addMemberToProject(projectId, emailDTO), HttpStatus.CREATED);
    }

    @GetMapping("{projectId}/members")
    public ResponseEntity<List<UserResponseDTO>> getMembersByProject(@PathVariable("projectId") String projectId) {
        return new ResponseEntity<>(projectService.findMembersByProjectId(projectId), HttpStatus.OK);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") String projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
