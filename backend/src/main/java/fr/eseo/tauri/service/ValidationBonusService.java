package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationBonusService {

    private final AuthService authService;
    private final ValidationBonusRepository validationBonusRepository;
    private final UserService userService;
    @Lazy
    private final BonusService bonusService;

    public ValidationBonus getValidationBonusByAuthorId(String token, Integer bonusId, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findByAuthorIdAndBonusId(bonusId, authorId);
    }

    public List<ValidationBonus> getAllValidationBonuses(String token, Integer bonusId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findAllByBonusId(bonusId);
    }

    public void createValidationBonus(String token, ValidationBonus validationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        validationBonus.bonus(bonusService.getBonusById(token, validationBonus.bonusId()));
        validationBonus.author(userService.getUserById(token, validationBonus.authorId()));

        validationBonusRepository.save(validationBonus);
    }

    public void deleteAllValidationBonuses(String token, Integer bonusId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        validationBonusRepository.deleteAllByBonusId(bonusId);

    }

}