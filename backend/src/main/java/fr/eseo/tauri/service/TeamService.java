package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.Project;

import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    private final GradeRepository gradeRepository;

    /**
     * Constructor for TeamService.
     * @param teamRepository the team repository
     * @param userRepository the user repository
     * @param projectRepository the project repository
     * @param studentRepository the student repository
     * @param gradeRepository the grade repository
     */
    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, ProjectRepository projectRepository, StudentRepository studentRepository, GradeRepository gradeRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
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

            // List<Grade> grades = gradeRepository.findByTeam(team.get());
            // for (Grade grade : grades) {
            //    gradeRepository.delete(grade);
            // }

            teamRepository.deleteById(id);
        }
    }

    /*@PostConstruct //Test function for the deleteTeam function
    public void initDataIfTableIsEmpty() {

        if(userRepository.count() == 0){
            User user = new User();
            User user2 = new User();
            userRepository.save(user);
            userRepository.save(user2);
        }

        if (teamRepository.count() == 0) {
            // Ajouter une ligne dans la table teams si elle est vide
            Team team = new Team();
            team.project(projectRepository.findById(1).get());
            if (userRepository.count() != 0){
                team.leader(userRepository.findById(Long.valueOf(1)).get());
            }
            teamRepository.save(team);
        }

        if(studentRepository.count() == 0){
            Student student = new Student();
            Student student2 = new Student();
            student.team(teamRepository.findById(1).get());
            student2.team(teamRepository.findById(1).get());
            studentRepository.save(student);
            studentRepository.save(student2);
        }

        if(gradeRepository.count() == 0){
            Grade grade = new Grade();
            //grade.team(teamRepository.findById(1).get());
            gradeRepository.save(grade);
        }

        if (teamRepository.count() != 0) {
            //this.deleteTeam(1);
        }

        if(userRepository.count() != 0){
            //UserService.deleteUser(1);
        }

    }*/

    /**
     * Update the leader of a team.
     * @param teamId the ID of the team
     * @param leaderId the ID of the new leader
     * @return the updated team if successful, otherwise null
     */
    public Team updateLeaderTeam(Integer teamId, Integer leaderId) {
        User leader = userRepository.findById(Long.valueOf(leaderId)).orElse(null);
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
        List<Student> men = this.studentRepository.findByGender(Gender.MAN);

        int nbWomen = women.size();
        int nbMen = men.size();
        int nbStudent = nbMen + nbWomen;

        // TODO : get the ACTUAL project (not the first one)
        Project project = this.projectRepository.getReferenceById(1);

        // Check if the number of students is enough to create the teams
        if (nbStudent < nbTeams || nbTeams < 1) {
            System.out.println("ERROR : TeamService.createTeams : Not enough students to create the teams");
            return null;
        }else {
            List<Team> teams = new ArrayList<>();

            System.out.println("INFO : TeamService.createTeams : nbTeams : " + nbTeams);
            // Create the teams
            for (int i = 0; i < nbTeams; i++) {
                Team team = new Team();
                team.name("Team " + (i + 1));
                team.project(project);
                this.teamRepository.save(team);
                teams.add(team);
            }

            System.out.println("INFO : TeamService.createTeams : nbWomen : " + nbWomen);
            // Assign women to the teams
            for (int i = 0; i < nbWomen/womenPerTeam; i++) {
                for (int y = 0; y < womenPerTeam; y++) {
                    Student woman = women.get(womenPerTeam * i + y);
                    woman.team(teams.get(i % nbTeams));
                    this.studentRepository.save(woman);
                }
            }

            // Calculate the next team index
            int teamIndex = nbWomen/womenPerTeam;
            int womenIndex = nbWomen/womenPerTeam * womenPerTeam;

            // Assign the remaining women to the next team and complete with men
            for (int i = 0; i < womenPerTeam; i++) {
                Student student;
                if (i <  nbWomen%womenPerTeam) {
                    student = women.get(womenIndex + i);
                }else {
                    student = men.get(i - nbWomen%womenPerTeam);
                }
                student.team(teams.get(teamIndex));
                this.studentRepository.save(student);
            }

            int menIndex = womenPerTeam - nbWomen%womenPerTeam;

            // Assign men to even the remaining teams
            for (int i = teamIndex + 1; i < nbTeams; i++) {
                int y = 0;
                while (y < womenPerTeam && menIndex + y < nbMen) {
                    Student student = men.get(menIndex + y);
                    student.team(teams.get(i));
                    this.studentRepository.save(student);
                    y++;
                }
            }

            System.out.println("INFO : TeamService.createTeams : menIndex : " + (nbMen - (nbStudent - nbTeams * womenPerTeam)));
            menIndex = nbMen - (nbStudent - nbTeams * womenPerTeam);

            // Assign men to the teams
            for (int i = menIndex; i < nbMen; i++) {
                Student student = men.get(i);
                student.team(teams.get(i % nbTeams));
                this.studentRepository.save(student);
            }

            return teams;
        }
    }
}
