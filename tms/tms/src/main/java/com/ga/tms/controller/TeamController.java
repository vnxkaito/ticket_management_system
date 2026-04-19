package com.ga.tms.controller;

import com.ga.tms.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;

public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
}
