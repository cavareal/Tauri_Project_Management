package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
