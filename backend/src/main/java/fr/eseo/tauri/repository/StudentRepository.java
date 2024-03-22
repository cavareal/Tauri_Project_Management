package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
