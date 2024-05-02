package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.GradeRepository;

import java.util.List;

import static fr.eseo.tauri.util.ListUtil.filter;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final AuthService authService;
    private final GradeRepository gradeRepository;
    private final UserService userService;
    @Lazy
    private final StudentService studentService;
    @Lazy
    private final SprintService sprintService;
    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final GradeTypeRepository gradeTypeRepository;
    @Lazy
    private final GradeTypeService gradeTypeService;
    private final UserRepository userRepository;
    private final TeamService teamService;

    public Grade getGradeById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("grade", id));
    }

    public List<Grade> getAllUnimportedGradesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrades"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeRepository.findAllUnimportedByProject(projectId);
    }

    public List<Grade> getAllImportedGradesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrades"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return gradeRepository.findAllImportedByProject(projectId);
    }

    public void createGrade(String token, Grade grade) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        grade.author(userService.getUserById(token, grade.authorId()));
        grade.sprint(sprintService.getSprintById(token, grade.sprintId()));
        grade.gradeType(gradeTypeService.getGradeTypeById(token, grade.gradeTypeId()));
        if (grade.gradeType().forGroup()) {
            grade.team(teamService.getTeamById(token, grade.teamId()));
        } else {
            grade.student(studentService.getStudentById(token, grade.studentId()));
        }
        gradeRepository.save(grade);
    }

    public void updateGrade(String token, Integer id, Grade updatedGrade) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        Grade grade = getGradeById(token, id);
        if (updatedGrade.value() != null) grade.value(updatedGrade.value());
        if (updatedGrade.comment() != null) grade.comment(updatedGrade.comment());
        if (updatedGrade.sprintId() != null) grade.sprint(sprintService.getSprintById(token, updatedGrade.sprintId()));
        if (updatedGrade.authorId() != null) grade.author(userService.getUserById(token, updatedGrade.authorId()));
        if (updatedGrade.studentId() != null)
            grade.student(studentService.getStudentById(token, updatedGrade.studentId()));
        if (updatedGrade.teamId() != null) grade.team(teamService.getTeamById(token, updatedGrade.teamId()));
        gradeRepository.save(grade);
    }

    public void deleteGrade(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getGradeById(token, id);
        gradeRepository.deleteById(id);
    }

    public void deleteAllGradesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        gradeRepository.deleteAllByProject(projectId);
    }

    /**
     * This method is used to update the mean of imported grades for each student.
     */
    public void updateImportedMean() {
        var students = studentRepository.findAll();
        var grades = filter(gradeRepository.findAll(), grade -> grade.student() != null);
        for (var student : students) {
            if (Boolean.TRUE.equals(student.bachelor())) continue;
            var studentGrades = filter(grades, grade -> grade.student().id().equals(student.id()) && grade.gradeType().imported() && !grade.gradeType().name().equalsIgnoreCase("mean") && !grade.gradeType().name().equalsIgnoreCase("average"));
            var mean = mean(studentGrades);
            gradeRepository.updateImportedMeanByStudentId(mean, student.id());
        }
        CustomLogger.info("Updated imported mean for all students.");
    }

    /**
     * This method calculates the mean of a list of grades. * * @param grades the list of Grade objects for which the mean is to be calculated * @return the mean of the grades, or 0 if there are no grades or all grades have a factor of 0
     */
    float mean(List<Grade> grades) {
        var total = 0f;
        var factors = 0f;
        for (var grade : grades) {
            total += grade.value() * grade.gradeType().factor();
            factors += grade.gradeType().factor();
        }
        if (factors == 0) return 0;
        return total / factors;
    }

    /**
     * This method is used to create a new Grade object and save it to the database. * * @param author the author of the grade * @param gradeType the type of the grade * @param student the student who received the grade * @param value the value of the grade * @param comment the comment for the grade * @return the created Grade object that has been saved to the database
     */
    public Grade createGrade(User author, GradeType gradeType, Student student, float value, String comment) {
        Grade grade = new Grade();
        grade.value(value);
        grade.comment(comment);
        grade.author(author);
        grade.gradeType(gradeType);
        grade.student(student);
        return gradeRepository.save(grade);
    }

    /**
     * This method is used to assign a grade to a team. * * @param teamName the name of the team to whom the grade is assigned * @param value the value of the grade to be assigned * @param gradeName the name of the grade type to be assigned
     */
    public void assignGradeToTeam(String teamName, Integer value, String gradeName, int userId) {
        Team team = teamRepository.findByName(teamName);
        GradeType gradeType = gradeTypeRepository.findByName(gradeName);
        User author = userRepository.findById(userId).orElse(null);
        if (team != null) {
            Grade grade = new Grade();
            grade.value(Float.valueOf(value));
            grade.gradeType(gradeType);
            grade.team(team);
            grade.author(author);
            gradeTypeRepository.save(gradeType);
            gradeRepository.save(grade);
        } else {
            throw new IllegalArgumentException("Team with name " + teamName + " has not been found.");
        }
    }

    /**
     * This method is used to assign a grade to a student. * * @param studentName the name of the student to whom the grade is assigned * @param value the value of the grade to be assigned * @param gradeName the name of the grade type to be assigned
     */
    public void assignGradeToStudent(String studentName, Integer value, String gradeName) {
        Student student = studentRepository.findByName(studentName);
        GradeType gradeType = gradeTypeRepository.findByName(gradeName);
        if (student != null) {
            Grade grade = new Grade();
            grade.value(Float.valueOf(value));
            grade.gradeType(gradeType);
            grade.student(student);
            gradeTypeRepository.save(gradeType);
            gradeRepository.save(grade);
        } else {
            CustomLogger.info("Student with name " + studentName + " not found.");
        }
    }

    /**
     * This method is used to create grades for a student from the provided grade types and values. * * @param student the student for whom the grades are created * @param valuesString the list of grade values as strings * @param gradeTypes the list of grade types * @param comment the comment for the grades
     */
    public void createGradesFromGradeTypesAndValues(Student student, List<String> valuesString, List<GradeType> gradeTypes, String comment) {
        List<Grade> grades = gradeRepository.findAll();
        for (int i = 0; i < valuesString.size(); i++) {
            String gradeValue = valuesString.get(i).trim();
            if (!gradeValue.isEmpty()) {
                if (i == 0) {
                    float gradeAsFloat = Float.parseFloat(gradeValue);
                    grades.add(createGrade(null, gradeTypes.get(0), student, gradeAsFloat, comment)); // First grade is the average grade
                } else {
                    try {
                        float gradeAsFloat = Float.parseFloat(gradeValue);
                        grades.add(createGrade(null, gradeTypes.get(i), student, gradeAsFloat, comment));
                    } catch (
                            NumberFormatException ignored) { // Do nothing // If the grade is not a number, it is ignored
                    }
                }
            }
        }
        CustomLogger.info("Successfully created grades for student " + student.name() + " from grade types and values contained in the provided CSV file.");
    }

    public Double getAverageGradesByGradeTypeByRoleType(int userId, RoleType roleType, String gradeTypeName) {
        Team team = teamRepository.findTeamByStudentId(userId);
        return gradeRepository.findAverageGradesByGradeType(team, gradeTypeName, roleType);
    }
}
