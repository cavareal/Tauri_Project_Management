package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final TeamRepository teamRepository;
    private final GradeTypeRepository gradeTypeRepository;


    /**
     * Constructor for TeamService.
     * @param teamRepository the team repository
     * @param gradeRepository the grade repository
     */
    @Autowired
    public GradeService(TeamRepository teamRepository, GradeRepository gradeRepository, StudentRepository studentRepository, GradeTypeRepository gradeTypeRepository) {
        this.teamRepository = teamRepository;
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.gradeTypeRepository = gradeTypeRepository;
    }

    /*@PostConstruct //Test function for the deleteTeam function
    public void initDataIfTableIsEmpty() {
        if(gradeRepository.count() == 0){
            Grade grade = new Grade();
            Grade grade2 = new Grade();
            Grade grade3 = new Grade();

            gradeRepository.save(grade);
            gradeRepository.save(grade2);
            gradeRepository.save(grade3);

            //gradeRepository.save(grade);
            //gradeRepository.save(grade2);
        }
    }*/

    public void updateImportedMean() {
        var students = studentRepository.findAll();
        var grades = gradeRepository.findAll();

        for (var student : students) {
            if (Boolean.TRUE.equals(student.bachelor())) continue;

            var studentGrades = grades.stream()
                    .filter(grade -> grade.student().id().equals(student.id()) && grade.gradeType().imported() && !grade.gradeType().name().equals("mean"))
                    .toList();

            var mean = mean(studentGrades);

            gradeRepository.updateImportedMeanByStudentId(mean, student.id());
        }
    }

    private float mean(List<Grade> grades) {
        var total = 0f;
        var factors = 0f;

        for (var grade : grades) {
            total += grade.value() * grade.gradeType().factor();
            factors += grade.gradeType().factor();
        }

        if (factors == 0) return 0;
        return total / factors;
    }

    public void assignGradeToTeam(String teamName, Integer value, String gradeName) {
        Team team = teamRepository.findByName(teamName);
        GradeType gradeType = gradeTypeRepository.findByName(gradeName);
        if (team != null) {
            Grade grade = new Grade();
            grade.value(Float.valueOf(value));
            grade.gradeType(gradeType);
            grade.team(team);
            gradeTypeRepository.save(gradeType);
            gradeRepository.save(grade);
        } else {
            throw new IllegalArgumentException("L'équipe avec le nom fourni n'a pas été trouvée.");
        }
    }

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
        }
    }

    public List<Object[]> getAverageGradesByGradeTypeByRoleType(int userId) {
        Team team = teamRepository.findTeamByStudentId(userId);
        return gradeRepository.findAverageGradesByGradeType(team.id());
    }

}
