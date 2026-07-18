package org.example.hrms.team.controller;

import org.example.hrms.team.dto.TeamMemberRequestDTO;
import org.example.hrms.team.dto.TeamRequestDTO;
import org.example.hrms.team.dto.TeamResponseDTO;
import org.example.hrms.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public TeamResponseDTO createTeam(@RequestBody TeamRequestDTO dto) {
        return teamService.createTeam(dto);
    }

    @GetMapping
    public List<TeamResponseDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping("/{teamId}/members")
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public ResponseEntity<String> addMembers(
            @PathVariable Long teamId,
            @RequestBody TeamMemberRequestDTO dto) {

        teamService.addMembers(teamId, dto);

        return ResponseEntity.ok("Members added successfully");
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<?> getTeamMembers(
            @PathVariable Long teamId) {

        return ResponseEntity.ok(
                teamService.getTeamMembers(teamId)
        );
    }
}