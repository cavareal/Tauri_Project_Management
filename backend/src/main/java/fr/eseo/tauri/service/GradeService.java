package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTeamsRepository;
import fr.eseo.tauri.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final TeamRepository teamRepository;
    private final GradeTeamsRepository gradeTeamRepository;

    /**
     * Constructor for TeamService.
     *
     * @param teamRepository      the team repository
     * @param gradeRepository     the grade repository
     * @param gradeTeamRepository the grade teams repository
     */
    @Autowired
    public GradeService(TeamRepository teamRepository, GradeRepository gradeRepository, GradeTeamsRepository gradeTeamRepository) {
        this.teamRepository = teamRepository;
        this.gradeRepository = gradeRepository;
        this.gradeTeamRepository = gradeTeamRepository;
    }

    public void attributeGradeToTeam(Team team, Grade grade) {
        GradeTeams gradeTeam = new GradeTeams();
        gradeTeam.team(team);
        gradeTeam.grade(grade);
        gradeTeamRepository.save(gradeTeam);
    }

    @PostConstruct
    public void initDataIfTableIsEmpty() {
        if (gradeRepository.count() == 0) {
            Grade grade = new Grade();
            gradeRepository.save(grade);
            if (teamRepository.count() != 0) {
                Optional<Team> optionalTeam = teamRepository.findById(1);
                if (optionalTeam.isPresent()) {
                    Team team = optionalTeam.get();
                    attributeGradeToTeam(team, grade);
                }
            }
        }
    }

    public Grade createGrade(User author, GradeType gradeType, Student student, double value, String comment) {
        Grade grade = new Grade();
        grade.value(value);
        grade.comment(comment);
        grade.author(author);
        grade.gradeType(gradeType);
        grade.student(student);
        return gradeRepository.save(grade);
    }


    public void createGradesFromGradeTypesAndValues(Student student, List<String> valuesString, List<GradeType> gradeTypes, String comment) {
        List<Grade> grades = gradeRepository.findAll();
        for (int i = 0; i < valuesString.size(); i++) {
            String gradeValue = valuesString.get(i).trim();
            if (!gradeValue.isEmpty()) {
                if (i == 0) {
                    double gradeAsDouble = Double.parseDouble(gradeValue);
                    grades.add(createGrade(null, gradeTypes.get(0), student, gradeAsDouble, comment)); //First grade is the average grade
                } else {
                    try {
                        double gradeAsDouble = Double.parseDouble(gradeValue);
                        grades.add(createGrade(null, gradeTypes.get(i), student, gradeAsDouble, comment));
                    } catch (NumberFormatException ignored) {
                        // Do nothing
                    }
                }
            }
        }
    }
}
