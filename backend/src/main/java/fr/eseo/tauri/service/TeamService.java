package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.TeamRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final AuthService authService;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final CommentRepository commentRepository;
    private final GradeRepository gradeRepository;
    private final GradeTypeRepository gradeTypeRepository;
    private final PresentationOrderService presentationOrderService;
    private final SprintService sprintService;
    @Lazy
    private final StudentService studentService;

    private static final String READ_PERMISSION = "readTeam";
    private static final String DELETE_PERMISSION = "deleteTeam";

    public Team getTeamById(Integer id) {
        return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("team", id));
    }

    public List<Team> getAllTeamsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return teamRepository.findAllByProject(projectId);
    }

    /**
     * Delete already existing teams in te project and then create teams with the given number of teams.
     * @param nbTeams the number of teams to create
     * @return a List<Teams> if teams are created, otherwise null
     */
    public List<Team> createTeams(String token, Integer projectId, Integer nbTeams) {
        if (nbTeams < 1) {
            CustomLogger.error("TeamService.createTeams : The number of teams to create must be greater than 0");
            throw new IllegalArgumentException("The number of teams to create must be greater than 0");
        }

        Project project = projectService.getProjectById(projectId);

        // Delete all previous teams
        if(!getAllTeamsByProject(token, projectId).isEmpty()){
            deleteAllTeamsByProject(token, projectId);
        }

        ArrayList<Team> teams = new ArrayList<>();

        // Create the teams
        for (int i = 0; i < nbTeams; i++) {
            Team team = new Team("Équipe " + (i + 1), project);
            this.teamRepository.save(team);
            teams.add(team);
        }

        return teams;
    }

    public void updateTeam(String token, Integer id, Team updatedTeam) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateTeam"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Team team = getTeamById(id);

        if (updatedTeam.name() != null) team.name(updatedTeam.name());
        if (updatedTeam.leaderId() != null) team.leader(userService.getUserById(updatedTeam.leaderId()));

        teamRepository.save(team);
    }

    public void deleteAllTeamsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        studentRepository.removeAllStudentsFromTeams(projectId);
        teamRepository.deleteAllByProject(projectId);
    }

    /**
     * This method retrieves the number of women in a team by the team's ID.
     *
     * @param id The ID of the team.
     * @return The number of women in the team if the team exists, otherwise 0.
     */
    public Integer getNbWomenByTeamId(String token, Integer id){
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getTeamById(id);
        return teamRepository.countWomenInTeam(id);
    }

    /**
     * This method retrieves the number of bachelor students in a team by the team's ID.
     *
     * @param id The ID of the team.
     * @return The number of bachelor students in the team if the team exists, otherwise 0.
     */
    public Integer getNbBachelorByTeamId(String token, Integer id){
        if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getTeamById(id);
        return teamRepository.countBachelorInTeam(id);
    }

    public List<Student> getStudentsByTeamId(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getTeamById(id);
        return studentRepository.findByTeam(id);
    }

    public List<Student> getStudentsByTeamIdOrdered(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        Team team = getTeamById(id);
        Sprint currentSprint = sprintService.getCurrentSprint(team.project().id());
        var students = studentRepository.findByTeam(id);
        if(currentSprint != null){
            var presentationOrder = presentationOrderService.getPresentationOrderByTeamIdAndSprintId(id, currentSprint.id());
            if(presentationOrder.size() == students.size()) students.sort(Comparator.comparingInt(presentationOrder::indexOf));
        }
        return studentRepository.findByTeam(id);
    }

    public Double getTeamAvgGrade(String token, Integer id) {
        Team team = getTeamById(id);
        return teamRepository.findAvgGradeByTeam(team);
    }

    public Criteria getCriteriaByTeamId(String token, Integer id, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getTeamById(id);
        boolean validateWoman = getNbWomenByTeamId(token, id) >= projectService.getProjectById(projectId).nbWomen();
        boolean validateBachelor = getNbBachelorByTeamId(token, id) >= 1;
        return new Criteria(getNbWomenByTeamId(token, id), getNbBachelorByTeamId(token, id), validateWoman, validateBachelor);
    }

    /**
     * Auto generate teams with students according to the given number of teams and the number of women per team.
     * FUTURE :  create teams with the same average grade
     */
    public void generateTeams(String token, Integer projectId, Project projectDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "createTeam"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        CustomLogger.info("TeamService.createTeams : Creating Teams");

        List<Student> women = this.studentRepository.findByGenderAndProjectId(Gender.WOMAN, projectId);
        List<Student> men = this.studentRepository.findByGenderOrderByBachelorAndImportedAvgDesc(Gender.MAN, projectId);
        int nbStudent = men.size() + women.size();
        Integer nbTeams = projectDetails.nbTeams();
        Integer womenPerTeam = projectDetails.nbWomen();

        // Check if the number of students is enough to create the teams
        if (nbStudent < nbTeams * womenPerTeam - 1) {
            CustomLogger.error("TeamService.generateTeams : Not enough students to create the teams");
            throw new IllegalArgumentException("Not enough students to create the teams");
        }
        projectService.updateProject(projectId, projectDetails);
        List<Team> teams = this.createTeams(token, projectId, nbTeams);
        this.fillTeams(teams, women, men, womenPerTeam, nbStudent, false, projectId);
    }

    /**
     * Assign teams to the students.
     * @param teams the list of empty teams to fill
     * @param women the list of women students
     * @param men the list of men students
     * @param womenPerTeam the number of women per team
     */
    public void fillTeams(List<Team> teams, List<Student> women, List<Student> men, Integer womenPerTeam, Integer nbStudent, boolean autoRatio, Integer projectId) {
        int nbTeams = teams.size();
        int nbWomen = women.size();

        int index;

        // Assign "womenPerTeam" women to the teams first then even the teams with men if needed
        if (!autoRatio) {
            for (int i = 0; i < nbTeams; i++) {
                for (int j = 0; j < womenPerTeam; j++) {
                    Student student;
                    Role role = new Role();
                    role.type(RoleType.TEAM_MEMBER);
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
        }

        // re-order the teams by average grade
        CustomLogger.info("Teams before sorting : " + teams);
        List<Team>sortedTeams = this.teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId);

        index = nbTeams * womenPerTeam;

        // Assign the remaining students evenly to the teams
        for (int i = index; i < nbStudent; i++) {
            if ((i - index) % nbTeams == 0) {
                sortedTeams = this.teamRepository.findAllOrderByAvgGradeOrderByAsc(projectId);
            }
            CustomLogger.info("Teams actuals : " + sortedTeams);

            Student student;
            Role role = new Role();
            role.type(RoleType.TEAM_MEMBER);

            if (i < nbWomen) {
                student = women.get(i);
                student.team(sortedTeams.get((i - index)% nbTeams));
            }else{
                student = men.get(i - nbWomen);
                student.team(sortedTeams.get((i - index)% nbTeams));
            }

            role.user(student);
            CustomLogger.info("studentEND : " + student);
            this.roleRepository.save(role);
            this.studentRepository.save(student);
        }
        CustomLogger.info("Teams have been filled with students");
    }

    public List<Comment> getFeedbacksByTeamAndSprint(String token, Integer teamId, Integer sprintId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return commentRepository.findAllByTeamIdAndSprintId(teamId, sprintId);
    }

    public Double getTeamTotalGrade(String token, Integer teamId, Integer sprintId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        List<GradeType> teacherGradedTeamGradeTypes = gradeTypeRepository.findTeacherGradedTeamGradeTypes();
        List<Double> teamGrades = new ArrayList<>();

        for (GradeType gradeType : teacherGradedTeamGradeTypes) {
            Double average = gradeRepository.findAverageByGradeTypeForTeam(teamId, sprintId, gradeType.name());
            if (average != null) {
                teamGrades.add(average);
            }

        }

        return formattedResult(teamGrades.stream().mapToDouble(Double::doubleValue).sum() / teamGrades.size());

    }

    public List<Double> getIndividualTotalGrades(String token, Integer id, Integer sprintId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrades"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        List<Student> students = getStudentsByTeamId(token, id);
        List<Double> individualGrades = new ArrayList<>();

        for(Student student : students){
            Double studentGradedTeamGrade = gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, GradeTypeName.GLOBAL_TEAM_PERFORMANCE.displayName());
            Double individualGrade = gradeRepository.findAverageByGradeTypeForStudent(student.id(), sprintId, GradeTypeName.INDIVIDUAL_PERFORMANCE.displayName());
            double result;
            if(studentGradedTeamGrade != null && individualGrade != null){
                result = (2*individualGrade + studentGradedTeamGrade)/3;
            }
            else if(studentGradedTeamGrade == null){
                result = individualGrade;
            } else {
                result = studentGradedTeamGrade;
            }
            individualGrades.add(formattedResult(result));

        }
        return individualGrades;
    }

    public List<Double> getSprintGrades(String token, Integer id, Integer sprintId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        double teamGrade = getTeamTotalGrade(token, id, sprintId);

        List<Student> students = getStudentsByTeamId(token, id);
        List<Double> sprintGrades = new ArrayList<>();

        for(int i = 0; i < students.size(); i++){
            List<Bonus> studentBonuses = studentService.getStudentBonuses(token, students.get(i).id(), sprintId);
            double result = 0.7*(Math.min(teamGrade + studentBonuses.stream().mapToDouble(Bonus::value).sum(), 20.0)) + 0.3*(getIndividualTotalGrades(token, id, sprintId)).get(i);
            sprintGrades.add(formattedResult(result));
        }
        if (sprintGrades.isEmpty()) {
            return Collections.singletonList(-1.0);
        }
        return sprintGrades;
    }

    public List<Double> getAverageSprintGrades(Integer sprintId){
        List<Team> teams = teamRepository.findAll();
        List<Double> averageSprintGrades = new ArrayList<>();
        for(Team team : teams){
            List<Double> sprintGrades = getSprintGrades("token", team.id(), sprintId);
            double average = sprintGrades.stream().mapToDouble(Double::doubleValue).sum() / sprintGrades.size();
            averageSprintGrades.add(formattedResult(average));
        }
        return averageSprintGrades;
    }

    private Double formattedResult(Double result) {
        return Double.parseDouble(String.format("%.2f", result).replace(',', '.'));
    }

}