package fr.eseo.tauri.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final GradeTeamsRepository gradeTeamRepository;

    /**
     * Constructor for TeamService.
     *
     * @param gradeRepository     the grade repository
     * @param gradeTeamRepository the grade teams repository
     */
    @Autowired
    public GradeService(GradeRepository gradeRepository, GradeTeamsRepository gradeTeamRepository) {
        this.gradeRepository = gradeRepository;
        this.gradeTeamRepository = gradeTeamRepository;
    }

    /**
     * This method is used to attribute a grade to a team.
     *
     * @param team the team to which the grade is attributed
     * @param grade the grade that is attributed to the team
     */
    public void attributeGradeToTeam(Team team, Grade grade) {
        GradeTeams gradeTeam = new GradeTeams();
        gradeTeam.team(team);
        gradeTeam.grade(grade);
        gradeTeamRepository.save(gradeTeam);
    }

    /**
     * This method is used to create a new Grade object and save it to the database.
     *
     * @param author the author of the grade
     * @param gradeType the type of the grade
     * @param student the student who received the grade
     * @param value the value of the grade
     * @param comment the comment for the grade
     * @return the created Grade object that has been saved to the database
     */
    public Grade createGrade(User author, GradeType gradeType, Student student, double value, String comment) {
        Grade grade = new Grade();
        grade.value(value);
        grade.comment(comment);
        grade.author(author);
        grade.gradeType(gradeType);
        grade.student(student);
        return gradeRepository.save(grade);
    }


    /**
     * This method is used to create grades for a student from the provided grade types and values.
     *
     * @param student the student for whom the grades are created
     * @param valuesString the list of grade values as strings
     * @param gradeTypes the list of grade types
     * @param comment the comment for the grades
     */
    public void createGradesFromGradeTypesAndValues(Student student, List<String> valuesString, List<GradeType> gradeTypes, String comment) {
        List<Grade> grades = gradeRepository.findAll();
        for (int i = 0; i < valuesString.size(); i++) {
            String gradeValue = valuesString.get(i).trim();
            if (!gradeValue.isEmpty()) {
                if (i == 0) {
                    double gradeAsDouble = Double.parseDouble(gradeValue);
                    grades.add(createGrade(null, gradeTypes.get(0), student, gradeAsDouble, comment));  // First grade is the average grade
                } else {
                    try {
                        double gradeAsDouble = Double.parseDouble(gradeValue);
                        grades.add(createGrade(null, gradeTypes.get(i), student, gradeAsDouble, comment));
                    } catch (NumberFormatException ignored) {
                        // Do nothing
                        // If the grade is not a number, it is ignored
                    }
                }
            }
        }
    }
}
