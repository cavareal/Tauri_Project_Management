package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.exception.ResourceNotFoundException;
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
    private final BonusService bonusService;

    public ValidationBonus getValidationBonusById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("validationBonus", id));
    }

    public List<ValidationBonus> getAllValidationBonusesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findAllByProject(projectId);
    }

    public void createValidationBonus(String token, ValidationBonus validationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationBonus.author(userService.getUserById(token, validationBonus.authorId()));
        validationBonus.bonus(bonusService.getBonusById(token, validationBonus.bonusId()));

        validationBonusRepository.save(validationBonus);
    }

    public void updateValidationBonus(String token, Integer id, ValidationBonus updatedValidationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        ValidationBonus validationBonus = getValidationBonusById(token, id);

        if (updatedValidationBonus.confirmed() != null) validationBonus.confirmed(updatedValidationBonus.confirmed());
        if (updatedValidationBonus.authorId() != null) validationBonus.author(userService.getUserById(token, updatedValidationBonus.authorId()));
        if (updatedValidationBonus.bonusId() != null) validationBonus.bonus(bonusService.getBonusById(token, updatedValidationBonus.bonusId()));

        validationBonusRepository.save(validationBonus);
    }

    public void deleteValidationBonus(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getValidationBonusById(token, id);
        validationBonusRepository.deleteById(id);
    }

    public void deleteAllValidationBonusesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationBonusRepository.deleteAllByProject(projectId);
    }
}