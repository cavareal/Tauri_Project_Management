package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationBonusService {

    private final AuthService authService;
    private final ValidationBonusRepository validationBonusRepository;
    private final TeamService teamService;

    public ValidationBonus getValidationBonusByAuthorId(String token, Integer bonusId, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findByAuthorIdAndBonusId(bonusId, authorId);
    }

    public List<ValidationBonus> getAllValidationBonuses(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findAllByBonusId(projectId);
    }

    public void createValidationBonuses(String token, Bonus bonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        if (Boolean.FALSE.equals(bonus.limited())) return;

        List<User> members = teamService.getMembersByTeamId(token, bonus.student().team().id());
        for(User member : members) {
            ValidationBonus validationBonus = new ValidationBonus();
            validationBonus.bonus(bonus);
            validationBonus.author(member);
            validationBonusRepository.save(validationBonus);
        }
    }

    public void updateValidationBonus(String token, Integer bonusId, Integer authorId, ValidationBonus updatedValidationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        var validationBonus = getValidationBonusByAuthorId(token, bonusId, authorId);
        if (updatedValidationBonus.confirmed() != null) validationBonus.confirmed(updatedValidationBonus.confirmed());
        validationBonusRepository.save(validationBonus);
    }

}