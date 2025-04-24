package com.everton.taskmanager.controllers;

import com.everton.taskmanager.entities.teams.*;
import com.everton.taskmanager.services.AttributeService;
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

    @Autowired
    private AttributeService attributeService;

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@PathVariable("organizationId") String organizationId, @RequestBody @Valid CreateTeamDTO team) {
        return new ResponseEntity<>(teamService.createTeam(organizationId, team), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> findAllTeams(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(teamService.findAllTeams(organizationId), HttpStatus.OK);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> findTeam(@PathVariable("teamId") String teamId) {
        return new ResponseEntity<>(teamService.findTeamById(teamId), HttpStatus.OK);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<TeamDTO> saveTeam(@PathVariable("teamId") String teamId, @RequestBody @Valid SaveTeamDTO team) {
        return new ResponseEntity<>(teamService.saveTeam(teamId, team), HttpStatus.OK);
    }

    @PatchMapping("/{teamId}/attributes")
    public ResponseEntity<TeamAttributesDTO> saveTeamAttributes(@PathVariable("teamId") String teamId, @RequestBody @Valid SaveTeamAttributesDTO attributesDTO) {
        return new ResponseEntity<>(attributeService.saveTeamAttributes(teamId, attributesDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable("teamId") String teamId) {

        teamService.deleteTeam(teamId);

        return new ResponseEntity<String>("Team deleted successfully.", HttpStatus.OK);
    }
}
