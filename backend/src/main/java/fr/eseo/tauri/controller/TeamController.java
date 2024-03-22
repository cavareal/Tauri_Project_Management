package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/add")
    public Team addTeam(@RequestBody Team team) {
        return teamRepository.save(team);
    }

    @GetMapping("/all")
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Team updateTeam(@PathVariable Integer id, @RequestBody Team teamDetails) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team != null) {
            team.name(teamDetails.name());
            return teamRepository.save(team);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Integer id) {
        teamRepository.deleteById(id);
        return "Team deleted";
    }
}
