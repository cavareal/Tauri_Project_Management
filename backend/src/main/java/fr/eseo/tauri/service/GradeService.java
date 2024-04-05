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
    private final GradeRepository gradeRepository;
    private final TeamRepository teamRepository;

    private final StudentRepository studentRepository;

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

    public void assignGradeToTeam(String teamName, Integer value, GradeType gradeType/*,User author, String comment*/) {
        Team team = teamRepository.findByName(teamName);
        if (team != null) {
            Grade grade = new Grade();
            grade.value(Float.valueOf(value));
            grade.gradeType(gradeType);
            grade.team(team);
//            grade.author(author);
//            if (comment != ""){
//                grade.comment(comment);
//            }
            gradeTypeRepository.save(gradeType);
            gradeRepository.save(grade);
        } else {
            throw new IllegalArgumentException("L'équipe avec le nom fourni n'a pas été trouvée.");
        }
    }

    public void assignGradeToStudent(String studentName, Integer value, GradeType gradeType/*,User author, String comment*/) {
        Student student = studentRepository.findByName(studentName);
        if (student != null) {
            Grade grade = new Grade();
            grade.value(Float.valueOf(value));
            grade.gradeType(gradeType);
            grade.student(student);
            //grade.author(author);
            gradeTypeRepository.save(gradeType);
            gradeRepository.save(grade);
        } else {
            // Gérer le cas où pas d'étudiant
        }
    }

}
