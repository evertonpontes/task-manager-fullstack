package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createOrganization(@RequestBody @Valid SaveGroupDTO organizationDTO) {
        return new ResponseEntity<>(organizationService.createOrganization(organizationDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getAllOrganizations() {
        return  new ResponseEntity<>(organizationService.findAllOrganizations(), HttpStatus.OK);
    }

    @GetMapping("{organizationId}")
    public ResponseEntity<GroupResponseDTO> getOrganization(@PathVariable("organizationId") String organizationId) {
        return  new ResponseEntity<>(organizationService.findByOrganizationId(organizationId), HttpStatus.OK);
    }

    @PutMapping("{organizationId}")
    public ResponseEntity<GroupResponseDTO> updateOrganization(@PathVariable("organizationId") String organizationId,
                                                                      @RequestBody @Valid SaveGroupDTO organizationDTO) {
        return new ResponseEntity<>(organizationService.updateOrganization(organizationId, organizationDTO), HttpStatus.OK);
    }

    @PostMapping("{organizationId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> addAttributesToOrganization(@PathVariable("organizationId") String organizationId,
                                                                                  @RequestBody @Valid SaveAllGroupAttributesDTO attributesDTO) {
        return new ResponseEntity<>(organizationService.addAttributesToOrganization(organizationId, attributesDTO), HttpStatus.CREATED);
    }

    @GetMapping("{organizationId}/members")
    public ResponseEntity<List<UserResponseDTO>> getMembersByOrganization(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(organizationService.findMembersByOrganizationId(organizationId), HttpStatus.OK);
    }

    @PostMapping("{organizationId}/members")
    public ResponseEntity<List<UserResponseDTO>> addMemberToOrganization(@PathVariable("organizationId") String organizationId,
                                                                                  @RequestBody @Valid MemberEmailDTO emailDTO) {
        return new ResponseEntity<>(organizationService.addMemberToOrganization(organizationId, emailDTO), HttpStatus.CREATED);
    }

    @GetMapping("{organizationId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> getAttributesByOrganization(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(organizationService.findAttributesByOrganizationId(organizationId), HttpStatus.OK);
    }

    @DeleteMapping("{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable("organizationId") String organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }
}
