package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeTeams;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
public class GradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradeService.class);
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

    @PostConstruct //Test function for the deleteTeam function
    public void initDataIfTableIsEmpty() {
        if (gradeRepository.count() == 0) {
            Grade grade = new Grade();
            gradeRepository.save(grade);
            if (teamRepository.count() != 0) {
                Team team = teamRepository.findById(1).get();
                attributeGradeToTeam(team, grade);
            }

        }
    }

    public void createGradeFromAverage(String average, Student student) {
        try {
            Grade grade = new Grade();
            grade.value(Double.valueOf(average.trim()));
            grade.student(student);
            gradeRepository.save(grade);
            LOGGER.info("Successfully created and saved grade for studentId: {}", student.id());
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating grade from average for studentId: {}", student.id(), e);
        }
    }


}
