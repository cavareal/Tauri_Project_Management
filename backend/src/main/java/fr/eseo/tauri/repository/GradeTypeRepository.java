package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.GradeType;

public interface GradeTypeRepository extends JpaRepository<GradeType, Integer> {
}
