package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
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
    private final UserService userService;

    public ValidationBonus getValidationBonusByAuthorId(String token, Integer bonusId, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findByAuthorIdAndBonusId(authorId, bonusId);
    }

    public List<ValidationBonus> getAllValidationBonuses(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findAllByBonusId(projectId);
    }

    //TODO Creer tous les validation bonuses associé au bonus limité passé en paramètre
    public void createValidationBonus(String token, ValidationBonus validationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationBonusRepository.save(validationBonus);
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