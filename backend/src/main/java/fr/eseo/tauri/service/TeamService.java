package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing teams.
 */
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;

    public void deleteAllTeams() {
        var teams = teamRepository.findAll();
        for (var team : teams) {
            deleteTeam(team.id());
        }
    }

    /**
     * Change students team to null when their team is deleted.
     * @param id the team's id
     */
    public void deleteTeam(Integer id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            List<Student> students = studentRepository.findByTeam(team.get());
            for (Student student : students) {
                student.team(null);
                studentRepository.save(student);
            }
            teamRepository.deleteById(id);
        } else {
            CustomLogger.logWarn("TeamService.deleteTeam : Team with id " + id + " not found");
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
     * Auto generate teams with students according to the given number of teams and the number of women per team.
     * FUTURE :  create teams with the same average grade
     * @param nbTeams the number of teams to create
     * @param womenPerTeam the ratio of women in the teams
     * @return a List<Teams> if teams are created, otherwise null
     */
    public List<Team> generateTeams(Integer nbTeams, Integer womenPerTeam) throws IllegalArgumentException{
        CustomLogger.logInfo("TeamService.createTeams : Creating Teams");

        List<Student> women = this.studentRepository.findByGender(Gender.WOMAN);
        List<Student> men = this.studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN);

        int nbStudent = men.size() + women.size();

        // Check if the number of students is enough to create the teams
        if (nbStudent < nbTeams * womenPerTeam - 1) {
            CustomLogger.logError("TeamService.generateTeams : Not enough students to create the teams");
            throw new IllegalArgumentException("Not enough students to create the teams");
        }else {
            List<Team> teams = this.createTeams(nbTeams);
            this.fillTeams(teams, women, men, womenPerTeam);
            return teams;
        }
    }

    /**
     * Delete already existing teams in te project and then create teams with the given number of teams.
     * // TODO : check nbTeams to be sure it is correct
     * @param nbTeams the number of teams to create
     * @return a List<Teams> if teams are created, otherwise null
     */
    private List<Team> createTeams(Integer nbTeams) throws IllegalArgumentException{
        if (nbTeams < 1) {
            CustomLogger.logError("TeamService.createTeams : The number of teams to create must be greater than 0");
            throw new IllegalArgumentException("The number of teams to create must be greater than 0");
        }

        Project project = this.projectService.getCurrentProject();

        // Delete all previous teams
        // TODO FUTURE : delete teams only when nbTeams is different from the number of teams in the project
        List<Team> teamsToDelete = this.teamRepository.findAllByProjectId(project.id());
        for (Team team : teamsToDelete) {
            this.deleteTeam(team.id());
        }

        ArrayList<Team> teams = new ArrayList<>();

        // Create the teams
        for (int i = 0; i < nbTeams; i++) {
            Team team = new Team();
            team.name("Team " + (i + 1));
            team.project(project);
            this.teamRepository.save(team);
            teams.add(team);
        }

        return teams;
    }

    /**
     * Assign teams to the students.
     * @param teams the list of empty teams to fill
     * @param women the list of women students
     * @param men the list of men students
     * @param womenPerTeam the number of women per team
     */
    private void fillTeams(List<Team> teams, List<Student> women, List<Student> men, Integer womenPerTeam) {
        int nbTeams = teams.size();
        int nbWomen = women.size();
        int nbMen = men.size();
        int nbStudent = nbMen + nbWomen;

        int index;

        Role role = new Role();
        role.type(RoleType.TEAM_MEMBER);

        // Assign "womenPerTeam" women to the teams first then even the teams with men if needed
        for (int i = 0; i < nbTeams; i++) {
            for (int j = 0; j < womenPerTeam; j++) {
                Student student;
                index = i * womenPerTeam + j;

                if (index < nbWomen) {
                    student = women.get(index);
                    student.team(teams.get(i));
                    role.user(student);
                    this.roleRepository.save(role);
                    this.studentRepository.save(student);
                } else if (index < nbStudent) {
                    student = men.get(index - nbWomen);
                    student.team(teams.get(i));
                    role.user(student);
                    this.roleRepository.save(role);
                    this.studentRepository.save(student);
                }
            }
        }

        // re-order the teams by average grade
        List<Team>sortedTeams = this.teamRepository.findAllOrderByAvgGradeOrderByAsc();

        index = nbTeams * womenPerTeam;

        // Assign the remaining students evenly to the teams
        for (int i = index; i < nbStudent; i++) {
            if ((i - index) % nbTeams == 0) {
                sortedTeams = this.teamRepository.findAllOrderByAvgGradeOrderByAsc();
            }

            Student student;
            if (i < nbWomen) {
                student = women.get(i);
                student.team(sortedTeams.get((i - index)% nbTeams));
            }else{
                student = men.get(i - nbWomen);
                student.team(sortedTeams.get((i - index)% nbTeams));
            }
            
            role.user(student);
            this.roleRepository.save(role);
            this.studentRepository.save(student);
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

    /**
     * This method retrieves the number of women in a team by the team's ID.
     *
     * @param id The ID of the team.
     * @return The number of women in the team if the team exists, otherwise 0.
     */
    public Integer getNbWomanByTeamId(Integer id){
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            List<Student> students = studentRepository.findByTeam(teamOptional.get());
            Integer nbWoman = 0;
            for (Student student : students) {
                if (student.gender() == Gender.WOMAN) {
                    nbWoman++;
                }
            }
            return nbWoman;
        } else {
            return null; // for example
        }
    }

    /**
     * This method retrieves the number of bachelor students in a team by the team's ID.
     *
     * @param id The ID of the team.
     * @return The number of bachelor students in the team if the team exists, otherwise 0.
     */
    public Integer getNbBachelorByTeamId(Integer id){
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            List<Student> students = studentRepository.findByTeam(teamOptional.get());
            Integer nbBachelor = 0;
            for (Student student : students) {
                if (student.bachelor() != null && student.bachelor()) {
                    nbBachelor++;
                }
            }
            return nbBachelor;
        } else {
            return null; // for example
        }
    }

    /**
     * This method retrieves the number of students in a team by the team's ID.
     *
     * @param id The ID of the team.
     * @return The number of students in the team if the team exists, otherwise 0.
     */
    public Integer getNbStudentsByTeamId(Integer id){
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            List<Student> students = studentRepository.findByTeam(teamOptional.get());
            return students.size();
        } else {
            return null; // for example
        }
    }

    public Team getTeamBySSId(Integer id){
        var teams = getAllTeams();
        for (var team : teams) {
            if (team.leader() != null && team.leader().id().equals(id)) {
                return team;
            }
        }
        return null;
    }

    public double getTeamAvgGrade(Integer idTeam) throws IllegalArgumentException {
        Optional<Team> optionalTeam = this.teamRepository.findById(idTeam);
        if (optionalTeam.isEmpty()) {
            throw new IllegalArgumentException("Team with id " + idTeam + " not found");
        }
        return this.teamRepository.findAvgGradeByTeam(optionalTeam.get());
    }
}
