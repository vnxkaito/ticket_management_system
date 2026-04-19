package com.ga.tms.service;

import com.ga.tms.exceptions.InformationExistException;
import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.Team;
import com.ga.tms.model.User;
import com.ga.tms.security.UserRepository;
import com.ga.tms.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team createTeam(Team team) {
        if (teamRepository.existsByName(team.getName())) {
            throw new InformationExistException("Team with name " + team.getName() + " already exists.");
        }
        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Team " + id + " not found"));
    }

    public Team getTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new InformationNotFoundException("No team found with name: " + name));
    }

    public Team updateTeam(Long id, Team teamDetails) {
        Team team = getTeamById(id);
        team.setName(teamDetails.getName());
        return teamRepository.save(team);
    }

    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.delete(team);
    }

    public Team addUserToTeam(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("Couldn't find user " + userId));
        user.getTeams().add(team);
        userRepository.save(user);
        return teamRepository.findById(teamId).get();
    }

    public Team removeUserFromTeam(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found: " + userId));
        user.getTeams().remove(team);
        userRepository.save(user);
        return teamRepository.findById(teamId).get();
    }

    public Set<User> getTeamMembers(Long teamId) {
        Team team = getTeamById(teamId);
        return team.getMembers();
    }
}
