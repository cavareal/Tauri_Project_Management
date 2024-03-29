package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param projectRepository the project repository
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
                student.teamId(null);
                studentRepository.save(student);
            }
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
            team.projectId(projectRepository.findById(1).get());
            if (userRepository.count() != 0){
                team.leaderId(userRepository.findById(Long.valueOf(1)).get());
            }
            teamRepository.save(team);
        }

        if(studentRepository.count() == 0){
            Student student = new Student();
            Student student2 = new Student();
            student.teamId(teamRepository.findById(1).get());
            student2.teamId(teamRepository.findById(1).get());
            studentRepository.save(student);
            studentRepository.save(student2);
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
            team.leaderId(leader);
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
     * @return true if teams are created, otherwise null
     */
    public Team createTeams() {

        Project project = projectRepository.findById(1).orElse(null);
        if(project == null){
            return null;
        }

        Integer ratioGender = project.ratioGender();
        Integer nbTeams = project.nbTeams();

        


        return null;
    }


}
