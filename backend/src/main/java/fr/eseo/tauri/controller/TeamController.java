package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Criteria;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing teams.
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamRepository teamRepository;
    private final AuthService authService;
    private final TeamService teamService;
    private final ProjectService projectService;
    /**
     * Constructor for TeamController.
     * @param teamRepository the team repository
     * @param authService    the authentication service
     * @param teamService    the team service
     */
    @Autowired
    public TeamController(TeamRepository teamRepository, AuthService authService, TeamService teamService, ProjectService projectService) {
        this.teamRepository = teamRepository;
        this.authService = authService;
        this.teamService = teamService;
        this.projectService = projectService;
    }

    /**
     * Update the leader of a team.
     *
     * @param token    the authorization token
     * @param idTeam   the ID of the team
     * @param idLeader the ID of the new leader
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PutMapping("/update-leader-team/{idTeam}")
    public ResponseEntity<String> updateLeaderTeam(@RequestHeader("Authorization") String token, @PathVariable Integer idTeam, @RequestParam Integer idLeader) {
        String permission = "teamCreation";
        if (authService.checkAuth(token, permission)) {
            try {
                Team team = teamService.updateLeaderTeam(idTeam, idLeader);
                if (team != null) {
                    return ResponseEntity.ok("La modification a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }


    /**
     * Update the name of a team.
     *
     * @param token   the authorization token
     * @param idTeam  the ID of the team
     * @param newName the new name of a team
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PutMapping("/update-name-team/{idTeam}")
    public ResponseEntity<String> updateNameTeam(@RequestHeader("Authorization") String token, @PathVariable Integer idTeam, @RequestParam String newName) {
        String permission = "teamRename";
        if (authService.checkAuth(token, permission)) {
            try {
                Team team = teamService.updateNameTeam(idTeam, newName);
                if (team != null) {
                    return ResponseEntity.ok("La modification a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }


    /**
     * Create teams.
     * @param token the authorization token
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PostMapping("/create-teams")
    public ResponseEntity<String> createTeams(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        Integer nbTeams = Integer.valueOf(request.get("nbTeams"));
        Integer womenPerTeam = Integer.valueOf(request.get("womenPerTeam"));
        String permission = "teamCreate";

        if (authService.checkAuth(token, permission)) {  // Check if role is authorised to do this request, in fonction of the permissions
            try {
                List<Team> teams = teamService.createTeams(nbTeams, womenPerTeam);

                if (teams != null) {
                    System.out.println("Teams have been created");
                    return ResponseEntity.ok("La creation a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : les équipes n'ont pas pu être créées");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

    /**
     * Get All Teams.
     *
     * @return A list of all teams
     */
    @GetMapping()
    public ResponseEntity<List<Team>> getAllTeams(@RequestHeader("Authorization") String token) {
        String permission = "readStudentByTeam";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                List<Team> teams = teamService.getAllTeams();
                return ResponseEntity.ok(teams);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Code 401
        }
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@RequestHeader("Authorization") String token, @PathVariable Integer teamId) {
        String permission = "readStudentByTeam";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                Team team = teamService.getTeamById(teamId);
                if (team == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                return ResponseEntity.ok(team);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Code 401
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllTeamNames(@RequestHeader("Authorization") String token) {
        String permission = "readStudentByTeam";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                List<String> teams = teamService.getAllTeamNames();
                if (teams.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teams);
                }
                return ResponseEntity.ok(teams);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Code 401
        }
    }

    @GetMapping("/{teamId}/criteria")
    public ResponseEntity<Criteria> getCriteriaByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer teamId) {
        String permission = "readCriteria";
        if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
            try {
                Integer nbWoman = teamService.getNbWomanByTeamId(teamId);
                Integer nbBachelor = teamService.getNbBachelorByTeamId(teamId);
                Integer nbStudents = teamService.getNbStudentsByTeamId(teamId);
                Integer womenPerTeam = projectService.getRatioGender();
                Boolean validateWoman = false;
                Boolean validateBachelor =false;
                if(nbStudents > 0 && (nbWoman*100)/nbStudents >= womenPerTeam){
                    validateWoman = true;
                }
                if (nbBachelor>=1){
                    validateBachelor = true;
                }
                Criteria criteria = new Criteria(nbWoman, nbBachelor, nbStudents, validateWoman, validateBachelor);
                return ResponseEntity.ok(criteria);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur 500
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Code 401
        }
    }
    @GetMapping("/get-team-avg-grade/{idTeam}")
    public ResponseEntity<String> getTeamAvgGrade(@RequestHeader("Authorization") String token, @PathVariable Integer idTeam) {
        String permission = "readTeamAvgGrade";
        if (authService.checkAuth(token, permission)) {
            try {
                double avgGrade = this.teamRepository.findAvgGradeByTeamId(this.teamRepository.findById(idTeam).get());
                return ResponseEntity.ok(String.valueOf(avgGrade));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }
}
