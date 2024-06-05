package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.service.PresentationOrderService;
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
    private final PresentationOrderService presentationOrderService;
    private final ResponseMessage responseMessage = new ResponseMessage("team");

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeamsByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Team> teams = teamService.getAllTeamsByProject(token, projectId);
        return ResponseEntity.ok(teams);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTeam(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Team updatedTeam) {
        teamService.updateTeam(token, id, updatedTeam);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllTeamsByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        teamService.deleteAllTeamsByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var students = teamService.getStudentsByTeamId(token, id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/orderedStudents")
    public ResponseEntity<List<Student>> getStudentsByTeamIdOrdered(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var students = teamService.getStudentsByTeamIdOrdered(token, id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/criteria")
    public ResponseEntity<Criteria> getCriteriaByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam("projectId") Integer projectId) {
        Criteria criteria = teamService.getCriteriaByTeamId(token, id, projectId);
        return ResponseEntity.ok(criteria);
    }

    @GetMapping("/{id}/average")
    public ResponseEntity<Double> getTeamAvgGrade(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        var avgGrade = this.teamService.getTeamAvgGrade(token, id);
        return ResponseEntity.ok(avgGrade);
    }

    /**
     * Create teams.
     *
     * @param token the authorization token
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PostMapping
    public ResponseEntity<String> generateTeams(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId, @Validated(Create.class) @RequestBody Project projectDetails) {
        teamService.generateTeams(token, projectId, projectDetails);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @GetMapping("/{teamId}/sprints/{sprintId}/feedbacks")
    public ResponseEntity<List<Comment>> getFeedbacksByTeamAndSprint(@RequestHeader("Authorization") String token, @PathVariable Integer teamId, @PathVariable Integer sprintId) {
        List<Comment> comment = teamService.getFeedbacksByTeamAndSprint(token, teamId, sprintId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}/sprint/{sprintId}/total")
    public ResponseEntity<Double> getTeamTotalGrade(@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable Integer sprintId) {
        Double totalGrade = teamService.getTeamTotalGrade(token, id, sprintId);
        return ResponseEntity.ok(totalGrade);
    }

    @GetMapping("/{id}/sprint/{sprintId}/individual/totals")
    public ResponseEntity<List<Double>> getIndividualTotalGrades(@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable Integer sprintId) {
        List<Double> studentsTotalGrades = teamService.getIndividualTotalGrades(token, id, sprintId);
        return ResponseEntity.ok(studentsTotalGrades);
    }

    @GetMapping("{id}/sprint/{sprintId}/grades")
    public ResponseEntity<List<Double>> getSprintGrades(@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable Integer sprintId) {
        List<Double> sprintGrade = teamService.getSprintGrades(token, id, sprintId);
        return ResponseEntity.ok(sprintGrade);
    }

    @GetMapping("/sprint/{sprintId}/average")
    public ResponseEntity<List<Double>> getAverageSprintGrades(@PathVariable Integer sprintId) {
        List<Double> sprintGrade = teamService.getAverageSprintGrades(sprintId);
        return ResponseEntity.ok(sprintGrade);
    }

    @GetMapping("/{id}/presentationOrder")
    public ResponseEntity<List<PresentationOrder>> getPresentationOrderByTeamIdAndSprintId(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam Integer sprintId) {
        List<PresentationOrder> presentationOrder = presentationOrderService.getPresentationOrderByTeamIdAndSprintId(token, id, sprintId);
        return ResponseEntity.ok(presentationOrder);
    }
}
