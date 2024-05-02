package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Criteria;
import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.service.TeamService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Controller class for managing teams.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@Tag(name = "teams")
public class TeamController {

    private final TeamService teamService;
    private final ResponseMessage responseMessage = new ResponseMessage("team");

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Team team = teamService.getTeamById(token, id);
        return ResponseEntity.ok(team);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeamsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        List<Team> teams = teamService.getAllTeamsByProject(token, projectId);
        return ResponseEntity.ok(teams);
    }

    /*@PostMapping
    public ResponseEntity<String> createTeam(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Team team) {
        teamService.createTeam(token, team);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }*/

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTeam(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Team updatedTeam) {
        teamService.updateTeam(token, id, updatedTeam);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllTeamsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        teamService.deleteAllTeamsByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }

    @GetMapping("/leader/{leaderId}")
    public ResponseEntity<Team> getTeamByLeaderId(@RequestHeader("Authorization") String token, @PathVariable Integer leaderId, @RequestParam Integer projectId) {
        Team team = teamService.getTeamByLeaderId(token, leaderId, projectId);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var students = teamService.getStudentsByTeamId(token, id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/criteria")
    public ResponseEntity<Criteria> getCriteriaByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam Integer projectId) {
        Criteria criteria = teamService.getCriteriaByTeamId(token, id, projectId);
        return ResponseEntity.ok(criteria);
    }

    @GetMapping("/{id}/average")
    public ResponseEntity<Double> getTeamAvgGrade(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        double avgGrade = this.teamService.getTeamAvgGrade(token, id);
        return ResponseEntity.ok(avgGrade);
    }

    /**
     * Create teams.
     *
     * @param token the authorization token
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PostMapping
    public ResponseEntity<String> generateTeams(@RequestHeader("Authorization") String token, @RequestParam Integer projectId, @Validated(Create.class) @RequestBody Project projectDetails) {
        teamService.generateTeams(token, projectId, projectDetails);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

}
