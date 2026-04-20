package com.ga.tms.controller;

import com.ga.tms.model.Team;
import com.ga.tms.model.User;
import com.ga.tms.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ga.tms.security.Roles;
import java.util.List;
import java.util.Set;

@Controller
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PreAuthorize("hasRole('" + Roles.ADMIN + "')")
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        return ResponseEntity.ok(teamService.createTeam(team));
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PreAuthorize("hasRole('" + Roles.ADMIN + "')")
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        return ResponseEntity.ok(teamService.updateTeam(id, team));
    }

    @PreAuthorize("hasRole('" + Roles.ADMIN + "')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team deleted successfully.");
    }

    @PreAuthorize("hasRole('" + Roles.ADMIN + "')")
    @PutMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Team> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.addUserToTeam(teamId, userId));
    }

    @PreAuthorize("hasRole('" + Roles.ADMIN + "')")
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Team> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.removeUserFromTeam(teamId, userId));
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @GetMapping("/{teamId}/members")
    public ResponseEntity<Set<User>> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamMembers(teamId));
    }


}
