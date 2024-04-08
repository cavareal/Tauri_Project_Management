package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing teams.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructor for TeamService.
     * @param teamRepository the team repository
     * @param userRepository the user repository
     * @param projectService the project repository
     * @param studentRepository the student repository
     */
    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, ProjectRepository projectRepository, StudentRepository studentRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
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
     * Create teams with the given number of teams and the given ratio
     * TODO : create teams with the same average grade
     * TODO : add a parameter to choose the project ?
     * @param nbTeams the number of teams to create
     * @param womenPerTeam the ratio of women in the teams
     * @return a List<Teams> if teams are created, otherwise null
     */
    public List<Team> createTeams(Integer nbTeams, Integer womenPerTeam) {
        System.out.println("INFO : TeamService.createTeams : Creating Teams");

        List<Student> women = this.studentRepository.findByGender(Gender.WOMAN);
        List<Student> men = this.studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN);

        int nbWomen = women.size();
        int nbMen = men.size();
        int nbStudent = nbMen + nbWomen;

        // TODO : get the ACTUAL project (not the first one)
        Project project = this.projectRepository.getReferenceById(1);

        // Check if the number of students is enough to create the teams
        if (nbStudent < nbTeams * womenPerTeam - 1) {
            System.out.println("ERROR : TeamService.createTeams : Not enough students to create the teams");
            return null;
        }else {
            List<Team> teams = new ArrayList<>();

            // Create the teams
            for (int i = 0; i < nbTeams; i++) {
                Team team = new Team();
                team.name("Team " + (i + 1));
                team.project(project);
                this.teamRepository.save(team);
                teams.add(team);
            }

            int index = 0;

            // Assign "womenPerTeam" women to the teams first then even the teams with men if needed
            for (int i = 0; i < nbTeams; i++) {
                for (int j = 0; j < womenPerTeam; j++) {
                    Student student = new Student();
                    index = i * womenPerTeam + j;
                    if (index < nbWomen) {
                        student = women.get(index);
                        student.team(teams.get(i));
                    } else if (index < nbStudent) {
                        student = men.get(index - nbWomen);
                        student.team(teams.get(i));
                    }
                    this.studentRepository.save(student);
                }
            }

            // re-order the teams by average grade
            teams = this.teamRepository.findAllOrderByAvgGradeOrderByAsc();

            index = nbTeams * womenPerTeam;

            // Assign the remaining students evenly to the teams
            for (int i = index; i < nbStudent; i++) {
                Student student;
                if (i < nbWomen) {
                    student = women.get(i);
                    student.team(teams.get((i - index)% nbTeams));
                }else{
                    student = men.get(i - nbWomen);
                    student.team(teams.get((i - index)% nbTeams));
                }
                this.studentRepository.save(student);
            }

            return teams;
        }
    }
}
