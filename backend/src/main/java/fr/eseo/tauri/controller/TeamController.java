package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Criteria;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.ProjectService;
import fr.eseo.tauri.service.TeamService;
import fr.eseo.tauri.util.CustomLogger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for managing teams.
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private static final String READ_STUDENT_BY_TEAM = "readStudentByTeam";
    private static final String READ_CRITERIA = "readCriteria";
    private static final String TEAM_CREATION = "teamCreation";

    private final TeamRepository teamRepository;
    private final AuthService authService;
    private final TeamService teamService;
    private final ProjectService projectService;

    /**
     * Constructor for TeamController.
     *
     * @param teamRepository the team repository
     * @param authService    the authentication service
     * @param teamService    the team service
     * @param projectService the projectService
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
        if (authService.checkAuth(token, TEAM_CREATION)) {
            try {
                Team team = teamService.updateLeaderTeam(idTeam, idLeader);
                if (team != null) {
                    return ResponseEntity.ok("La modification a bien été prise en compte");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour du leader de l'équipe");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour du leader de l'équipe: " + e.getMessage());
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
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour du nom de l'équipe");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour du nom de l'équipe : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }


    /**
     * Create teams.
     *
     * @param token the authorization token
     * @return a response entity with a success message if the update was successful, otherwise an error message
     */
    @PostMapping("/create-teams")
    public ResponseEntity<String> createTeams(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        Integer nbTeams = Integer.valueOf(request.get("nbTeams"));
        Integer womenPerTeam = Integer.valueOf(request.get("womenPerTeam"));

        if (authService.checkAuth(token, TEAM_CREATION)) {

            try {
                List<Team> teams = teamService.generateTeams(nbTeams, womenPerTeam);

                if (teams != null) {
                    CustomLogger.logInfo("Teams have been created");
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
        if (Boolean.TRUE.equals(authService.checkAuth(token, READ_STUDENT_BY_TEAM))) {
            try {
                List<Team> teams = teamService.getAllTeams();
                return ResponseEntity.ok(teams);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@RequestHeader("Authorization") String token, @PathVariable Integer teamId) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, READ_STUDENT_BY_TEAM))) {
            try {
                Team team = teamService.getTeamById(teamId);
                if (team == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                return ResponseEntity.ok(team);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllTeamNames(@RequestHeader("Authorization") String token) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, READ_STUDENT_BY_TEAM))) {
            try {
                List<String> teams = teamService.getAllTeamNames();
                if (teams.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teams);
                }
                return ResponseEntity.ok(teams);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/{teamId}/criteria")
    public ResponseEntity<Criteria> getCriteriaByTeamId(@RequestHeader("Authorization") String token, @PathVariable Integer teamId) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, READ_CRITERIA))) {
            try {
                Integer nbWoman = teamService.getNbWomanByTeamId(teamId);
                Integer nbBachelor = teamService.getNbBachelorByTeamId(teamId);
                Integer nbStudents = teamService.getNbStudentsByTeamId(teamId);
                Criteria criteria = getCriteria(nbStudents, nbWoman, nbBachelor);
                return ResponseEntity.ok(criteria);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @NotNull
    private Criteria getCriteria(Integer nbStudents, Integer nbWoman, Integer nbBachelor) {
        Integer womenPerTeam = projectService.getRatioGender();
        boolean validateWoman = false;
        boolean validateBachelor = false;
        if (nbStudents > 0 && (nbWoman * 100) / nbStudents >= womenPerTeam) {
            validateWoman = true;
        }
        if (nbBachelor >= 1) {
            validateBachelor = true;
        }
        return new Criteria(nbWoman, nbBachelor, nbStudents, validateWoman, validateBachelor);
    }

    @GetMapping("/{idTeam}/average")
    public ResponseEntity<String> getTeamAvgGrade(@RequestHeader("Authorization") String token, @PathVariable Integer idTeam) {
        String permission = "readTeamAvgGrade";
        if (authService.checkAuth(token, permission)) {
            try {
                if (this.teamRepository.findById(idTeam).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'équipe n'existe pas");
                } else {
                    Optional<Team> optionalTeam = this.teamRepository.findById(idTeam);
                    if (optionalTeam.isPresent()) {
                        double avgGrade = this.teamRepository.findAvgGradeByTeamId(optionalTeam.get());
                        return ResponseEntity.ok(String.valueOf(avgGrade));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'équipe n'existe pas");
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllTeams(@RequestHeader("Authorization") String token) {
        String permission = "teamDelete";
        if (authService.checkAuth(token, permission)) {
            try {
                teamService.deleteAllTeams();
                return ResponseEntity.ok("Les équipes ont bien été supprimées");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression : " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorisé");
        }
    }

    @GetMapping("/ss/{ssId}")
    public ResponseEntity<Team> getTeamBySupervisor(@RequestHeader("Authorization") String token, @PathVariable Integer ssId) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readTeamBySupervisor"))){
            try {
                Team team = teamService.getTeamBySSId(ssId);
                return ResponseEntity.ok(team);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
