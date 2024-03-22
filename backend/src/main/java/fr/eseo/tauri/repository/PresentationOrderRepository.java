package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.PresentationOrder;

public interface PresentationOrderRepository extends JpaRepository<PresentationOrder, Integer> {
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
}
