package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.BonusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final AuthService authService;
    private final BonusRepository bonusRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final SprintService sprintService;

    /**
     * Get a bonus by its id
     * @param token the token of the user
     * @param id the id of the bonus
     * @return the bonus
     */
    public Bonus getBonusById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bonus", id));
    }

    /**
     * Get all bonuses by project
     * @param token the token of the user
     * @param projectId the id of the project
     * @return the list of bonuses
     */
    public List<Bonus> getAllBonusesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findAllByProject(projectId);
    }

    /**
     * Create a bonus
     * @param token the token of the user
     * @param bonus the bonus to create
     */
    public void createBonus(String token, Bonus bonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        bonus.author(userService.getUserById(token, bonus.authorId()));
        bonus.student(studentService.getStudentById(token, bonus.studentId()));
        bonus.sprint(sprintService.getSprintById(token, bonus.sprintId()));

        bonusRepository.save(bonus);
    }

    /**
     * Update a bonus
     * @param token the token of the user
     * @param id the id of the bonus
     * @param updatedBonus the updated bonus
     */
    public void updateBonus(String token, Integer id, Bonus updatedBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Bonus bonus = getBonusById(token, id);

        if (updatedBonus.value() != null) bonus.value(updatedBonus.value());
        if (updatedBonus.comment() != null) bonus.comment(updatedBonus.comment());
        if (updatedBonus.limited() != null) bonus.limited(updatedBonus.limited());
        if (updatedBonus.sprintId() != null) bonus.sprint(sprintService.getSprintById(token, updatedBonus.sprintId()));
        if (updatedBonus.authorId() != null) bonus.author(userService.getUserById(token, updatedBonus.authorId()));
        if (updatedBonus.studentId() != null) bonus.student(studentService.getStudentById(token, updatedBonus.studentId()));

        bonusRepository.save(bonus);
    }

    /**
     * Delete a bonus with its id
     * @param token the token of the user
     * @param id the id of the bonus
     */
    public void deleteBonus(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getBonusById(token, id);
        bonusRepository.deleteById(id);
    }

    /**
     * Delete all bonuses by project
     * @param token the token of the user
     * @param projectId the id of the project
     */
    public void deleteAllBonusesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        bonusRepository.deleteAllByProject(projectId);
    }

}
