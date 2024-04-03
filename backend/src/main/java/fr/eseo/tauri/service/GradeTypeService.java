package fr.eseo.tauri.service;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeTypeService {

    private final GradeTypeRepository gradeTypeRepository;

    @Autowired
    public GradeTypeService(GradeTypeRepository gradeTypeRepository) {
        this.gradeTypeRepository = gradeTypeRepository;
    }

    public void createGradeType(GradeType gradeType) {
        if (gradeType.name() == null || gradeType.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        gradeTypeRepository.save(gradeType);
    }
}
