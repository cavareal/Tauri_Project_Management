package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
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
    private final BonusRepository bonusRepository;

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

        if(grade.authorId() != null) grade.author(userService.getUserById(token, grade.authorId()));
        if(grade.sprintId() != null) grade.sprint(sprintService.getSprintById(token, grade.sprintId()));
        if(grade.gradeTypeId() != null) grade.gradeType(gradeTypeService.getGradeTypeById(token, grade.gradeTypeId()));
        if (Boolean.TRUE.equals(grade.gradeType().forGroup())) {
            grade.student(null);
            if(grade.teamId() != null) grade.team(teamService.getTeamById(token, grade.teamId()));
        } else {
            grade.team(null);
            if(grade.studentId() != null) grade.student(studentService.getStudentById(token, grade.studentId()));
        }

        if ((grade.team() == null) == (grade.student() == null)) {
            throw new IllegalArgumentException("Both team and student attributes cannot be either null or not null at the same time");
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
        if (updatedGrade.studentId() != null) grade.student(studentService.getStudentById(token, updatedGrade.studentId()));
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
            var studentGrades = filter(grades, grade -> grade.student().id().equals(student.id()) && grade.gradeType().imported() && /*!grade.gradeType().name().equalsIgnoreCase("mean") && !grade.gradeType().name().equalsIgnoreCase("average")*/!grade.gradeType().name().equals(GradeTypeName.AVERAGE.displayName()));
            var mean = mean(studentGrades);
            gradeRepository.updateImportedMeanByStudentId(mean, student.id());
        }
        CustomLogger.info("Updated imported mean for all students.");
    }

    /**
     * This method calculates the mean of a list of grades. * * @param grades the list of Grade objects for which the mean is to be calculated * @return the mean of the grades, or 0 if there are no grades or all grades have a factor of 0
     */
    public float mean(List<Grade> grades) {
        var total = 0f;
        var factors = 0f;
        for (var grade : grades) {
            total += grade.value() * grade.gradeType().factor();
            factors += grade.gradeType().factor();
        }
        if (factors == 0) return 0;
        return total / factors;
    }

    public Double getAverageGradesByGradeTypeByRoleType(int userId, RoleType roleType, String gradeTypeName) {
        Team team = teamRepository.findTeamByStudentId(userId);
        return gradeRepository.findAverageGradesByGradeType(team, gradeTypeName, roleType);
    }

	public Float getGradeByStudentAndGradeType(Student student, GradeType gradeType) {
		try {
			Float grade = gradeRepository.findValueByStudentAndGradeType(student, gradeType);
			CustomLogger.info("Getting grade for student " + student.name() + " and grade type " + gradeType.name() + ": " + grade);
			return grade;
		} catch (NullPointerException e) {
			CustomLogger.info("No grade found for student " + student.name() + " and grade type " + gradeType.name());
			return null;
		}
	}

    public Double getAverageGradeTypeByStudentIdOrTeamId(Integer id, Integer sprintId, String gradeTypeName) {
        GradeType gradeType = gradeTypeRepository.findByName(gradeTypeName);
        Double grade;
        if (Boolean.TRUE.equals(gradeType.forGroup())) {
            grade = gradeRepository.findAverageByGradeTypeForTeam(id, sprintId, gradeTypeName);
        } else {
            grade = gradeRepository.findAverageByGradeTypeForStudent(id, sprintId, gradeTypeName);
        }
        return grade;
    }

    public Double getSprintGrade(String token, Integer userId, Integer sprintId) {
    /*    if (!Boolean.TRUE.equals(authService.checkAuth(token, "readGrade"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Student student = studentService.getStudentById(token, userId);

        Double sprintGrade;

        var grade1 = gradeRepository.findAverageByGradeTypeForTeam(student.teamId(), GradeTypeName.TECHNICAL_SOLUTION.displayName());
        var grade2 = gradeRepository.findAverageByGradeTypeForTeam(student.teamId(), GradeTypeName.PROJECT_MANAGEMENT.displayName());
        var grade3 = gradeRepository.findAverageByGradeTypeForTeam(student.teamId(), GradeTypeName.SPRINT_CONFORMITY.displayName());
        var grade4 = gradeRepository.findAverageByGradeTypeForTeam(student.teamId(), GradeTypeName.CONTENT_PRESENTATION.displayName());



        var bonuses = bonusRepository.findAllStudentBonuses(userId);

        //Student student = studentService.getStudentById(token, userId);
        //Sprint sprint = sprintService.getSprintById(token, sprintId);

        return gradeRepository.findSprintGrade(student, sprint);*/
        return 0.0;
    }
}
