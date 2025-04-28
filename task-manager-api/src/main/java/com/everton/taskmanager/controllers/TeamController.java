package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.groups.*;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.services.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations/{organizationId}/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createTeam(@PathVariable("organizationId") String organizationId,
                                                       @RequestBody @Valid SaveGroupDTO teamDTO) {
        return new ResponseEntity<>(teamService.createTeam(organizationId, teamDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getAllTeamsByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return  new ResponseEntity<>(teamService.findAllTeamsByOrganizationId(organizationId), HttpStatus.OK);
    }

    @GetMapping("{teamId}")
    public ResponseEntity<GroupResponseDTO> getTeam(@PathVariable("teamId") String teamId) {
        return  new ResponseEntity<>(teamService.findByTeamId(teamId), HttpStatus.OK);
    }

    @PutMapping("{teamId}")
    public ResponseEntity<GroupResponseDTO> updateTeam(@PathVariable("teamId") String teamId,
                                                      @RequestBody @Valid SaveGroupDTO teamDTO) {
        return new ResponseEntity<>(teamService.updateTeam(teamId, teamDTO), HttpStatus.OK);
    }

    @PostMapping("{teamId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> addAttributesToTeam(@PathVariable("teamId") String teamId,
                                                                                  @RequestBody @Valid SaveAllGroupAttributesDTO attributesDTO) {
        return new ResponseEntity<>(teamService.addAttributesToTeam(teamId, attributesDTO), HttpStatus.CREATED);
    }

    @GetMapping("{teamId}/attributes")
    public ResponseEntity<GroupAttributesResponseDTO> getAttributesByTeam(@PathVariable("teamId") String teamId) {
        return new ResponseEntity<>(teamService.findAttributesByTeamId(teamId), HttpStatus.OK);
    }

    @PostMapping("{teamId}/members")
    public ResponseEntity<List<UserResponseDTO>> addMemberToTeam(@PathVariable("teamId") String teamId,
                                                                 @RequestBody @Valid MemberEmailDTO emailDTO) {
        return new ResponseEntity<>(teamService.addMemberToTeam(teamId, emailDTO), HttpStatus.CREATED);
    }

    @GetMapping("{teamId}/members")
    public ResponseEntity<List<UserResponseDTO>> getMembersByTeam(@PathVariable("teamId") String teamId) {
        return new ResponseEntity<>(teamService.findMembersByTeamId(teamId), HttpStatus.OK);
    }

    @DeleteMapping("{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("teamId") String teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
