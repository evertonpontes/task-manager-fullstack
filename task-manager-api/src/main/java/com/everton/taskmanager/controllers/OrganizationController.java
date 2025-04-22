package com.everton.taskmanager.controllers;

import com.everton.taskmanager.entities.organization.OrgResponseDTO;
import com.everton.taskmanager.entities.organization.OrgToCreateDTO;
import com.everton.taskmanager.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/organization/{userId}")
    public ResponseEntity<OrgResponseDTO> createOrganization(@PathVariable("userId") String userId, @RequestBody @Valid OrgToCreateDTO orgToCreateDTO) {
        return new ResponseEntity<>(organizationService.createOrganization(userId, orgToCreateDTO), HttpStatus.CREATED);
    }
}
