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

    @PostConstruct //Test function for the deleteTeam function
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
            Team team2 = new Team();
            team.projectId(projectRepository.findById(1).get());
            team.name("Tauri");
            team2.projectId(projectRepository.findById(1).get());
            team2.name("LesAutres");
            if (userRepository.count() != 0){
                team.leaderId(userRepository.findById(Long.valueOf(1)).get());
                team2.leaderId(userRepository.findById(Long.valueOf(1)).get());
            }
            teamRepository.save(team);
            teamRepository.save(team2);
        }

        if(studentRepository.count() == 0){
            Student student = new Student();
            Student student2 = new Student();
            student.name("toto");
            student2.name("tata");
            student.teamRole("SM");
            student2.teamRole("PO");
            student.gender(Gender.MAN);
            student2.gender(Gender.OTHER);
            student.teamId(teamRepository.findById(1).get());
            student2.teamId(teamRepository.findById(1).get());
            Student student3 = new Student();
            Student student4 = new Student();
            student3.name("titi");
            student4.name("tete");
            student3.teamRole("SM");
            student4.teamRole("PO");
            student3.gender(Gender.MAN);
            student4.gender(Gender.OTHER);
            student3.teamId(teamRepository.findById(2).get());
            student4.teamId(teamRepository.findById(2).get());
            studentRepository.save(student);
            studentRepository.save(student2);
            studentRepository.save(student3);
            studentRepository.save(student4);
        }

        if (teamRepository.count() != 0) {
            //this.deleteTeam(1);
        }

        if(userRepository.count() != 0){
            //UserService.deleteUser(1);
        }

    }

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
     *
     * @return .. if teams are created, otherwise null
     */
    @Async
    public Team createTeams(Integer nbTeams, Integer ratioGender) {
        System.out.println("Create teams");
        System.out.println(nbTeams);
        System.out.println(ratioGender);

        // Set logic code to generate teams
        // You can delete this try/catch. It's use ti simulate loader on the frontend
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Team();
    }

    /**
     * Get all teams.
     * @return the list of all teams
     */
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Get a team by its ID.
     * @param id the ID of the team
     * @return the team if it exists, otherwise null
     */
    public Team getTeamById(Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

}
