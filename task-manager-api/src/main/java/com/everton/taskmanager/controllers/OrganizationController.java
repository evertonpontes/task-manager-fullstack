package com.everton.taskmanager.controllers;

import com.everton.taskmanager.entities.organization.*;
import com.everton.taskmanager.services.AttributeService;
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

    @Autowired
    private AttributeService attributeService;

    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody @Valid CreateOrganizationDTO organization) {
        return new ResponseEntity<>(organizationService.createOrganization(organization), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> findAllOrganizations() {
        return new ResponseEntity<>(organizationService.findAllOrganizations(), HttpStatus.OK);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> findOrganization(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(organizationService.findOrganization(organizationId), HttpStatus.OK);
    }

    @PatchMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> saveOrganization(@PathVariable("organizationId") String organizationId, @RequestBody @Valid SaveOrganizationDTO organization) {
        return new ResponseEntity<>(organizationService.saveOrganization(organizationId, organization), HttpStatus.OK);
    }

    @PatchMapping("/{organizationId}/attributes")
    public ResponseEntity<OrganizationAttributesDTO> saveOrganizationAttributes(@PathVariable("organizationId") String organizationId, @RequestBody @Valid SaveOrganizationAttributesDTO attributesDTO) {
        return new ResponseEntity<>(attributeService.saveOrganizationAttributes(organizationId, attributesDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<String> deleteOrganization(@PathVariable("organizationId") String organizationId) {

        organizationService.deleteOrganization(organizationId);

        return new ResponseEntity<String>("Organization deleted successfully.", HttpStatus.OK);
    }
}
