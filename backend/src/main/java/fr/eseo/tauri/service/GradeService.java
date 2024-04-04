package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final TeamRepository teamRepository;


    /**
     * Constructor for TeamService.
     * @param teamRepository the team repository
     * @param gradeRepository the grade repository
     */
    @Autowired
    public GradeService(TeamRepository teamRepository, GradeRepository gradeRepository) {
        this.teamRepository = teamRepository;
        this.gradeRepository = gradeRepository;
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

}
