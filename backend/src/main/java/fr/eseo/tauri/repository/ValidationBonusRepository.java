package fr.eseo.tauri.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.eseo.tauri.model.ValidationBonus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ValidationBonusRepository extends JpaRepository<ValidationBonus, Integer> {

    @Query("SELECT vb FROM ValidationBonus vb WHERE vb.author.id = :authorId AND vb.bonus.id = :bonusId")
    ValidationBonus findByAuthorIdAndBonusId(Integer bonusId, Integer authorId);

    @Query("SELECT vb FROM ValidationBonus vb WHERE vb.bonus.id = :bonusId")
    List<ValidationBonus> findAllByBonusId(Integer bonusId);

}
