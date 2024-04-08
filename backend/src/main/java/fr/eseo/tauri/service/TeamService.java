package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.ArrayUtils.isEquals;

/**
 * Service class for managing teams.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final StudentRepository studentRepository;

    /**
     * Constructor for TeamService.
     * @param teamRepository the team repository
     * @param userRepository the user repository
     * @param projectService the project repository
     * @param studentRepository the student repository
     */
    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, ProjectService projectService, StudentRepository studentRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.studentRepository = studentRepository;
    }

    /**
     * Change students team to null when their team is deleted.
     * @param id the team's id
     */
    public void deleteTeam(Integer id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            List<Student> students = studentRepository.findByTeamId(team.get());
            for (Student student : students) {
                student.team(null);
                studentRepository.save(student);
            }
            teamRepository.deleteById(id);
        }
    }

    /**
     * Update the leader of a team.
     * @param teamId the ID of the team
     * @param leaderId the ID of the new leader
     * @return the updated team if successful, otherwise null
     */
    public Team updateLeaderTeam(Integer teamId, Integer leaderId) {
        User leader = userRepository.findById(leaderId).orElse(null);
        Team team = teamRepository.findById(teamId).orElse(null);
        if (team != null && leader != null) {
            team.leader(leader);
            return teamRepository.save(team);
        }
        return null;
    }

    /**
     * Update the name of a team.
     * @param teamId the ID of the team
     * @param newName the new name of the team
     * @return the updated team if successful, otherwise null
     */
    public Team updateNameTeam(Integer teamId, String newName) {

        Team team = teamRepository.findById(teamId).orElse(null);
        if (team != null) {
            team.name(newName);
            return teamRepository.save(team);
        }
        return null;
    }


    /**
     * Create teams.
     *
     * @return .. if teams are created, otherwise null
     */
    @Async
    public void createTeams(Integer nbTeams, Integer ratioGender) {
        var project = projectService.getCurrentProject();

        var teams = new ArrayList<Team>();
        for (int i = 0; i < nbTeams; i++) {
            Team team = new Team();
            team.project(project);
            team.name("Équipe " + (i + 1));
            teams.add(team);
            teamRepository.save(team);
        }

        var students = studentRepository.findAll();
        for (int i = 0; i < students.size(); i++) {
            var student = students.get(i);
            student.team(teams.get(i % nbTeams));
            studentRepository.save(student);
        }

        var teamsToDelete = teamRepository.findAllByProjectId(project.id());
        for (var team : teamsToDelete) {
            if (teams.contains(team)) continue;
            teamRepository.deleteById(team.id());
        }
    }

    public List<String> getAllTeamNames() {
        return teamRepository.findAllTeamNames();
    }

    /**
     * Get all teams.
     * @return the list of all teams
     */
    public List<Team> getAllTeams() {
        var project = projectService.getCurrentProject();
        return teamRepository.findAllByProjectId(project.id());
    }

    /**
     * Get a team by its ID.
     * @param id the ID of the team
     * @return the team if it exists, otherwise null
     */
    public Team getTeamById(Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

    public Integer getNbWomanByTeamId(Integer id){
        List<Student> students = studentRepository.findByTeam(teamRepository.findById(id).get());
        Integer nbWoman = 0;
        for (Student student : students) {
            if (isEquals(student.gender(), Gender.WOMAN)) {
                nbWoman++;
            }
        }
        return nbWoman;
    }

    public Integer getNbBachelorByTeamId(Integer id){
        List<Student> students = studentRepository.findByTeam(teamRepository.findById(id).get());
        Integer nbBachelor = 0;
        for (Student student : students) {
            if (student.bachelor() != null && student.bachelor()) {
                nbBachelor++;
            }
        }
        return nbBachelor;
    }

    public Integer getNbStudentsByTeamId(Integer id){
        List<Student> students = studentRepository.findByTeam(teamRepository.findById(id).get());
        return students.size();
    }
}


