package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import fr.eseo.tauri.util.ListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationBonusService {

    private final AuthService authService;
    private final ValidationBonusRepository validationBonusRepository;
    private final UserService userService;

    public Boolean checkUserValidatedById(String token, Integer bonusId, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        var validationBonus = validationBonusRepository.findByAuthorIdAndBonusId(authorId, bonusId);
        return validationBonus != null;
    }

    public List<User> getAllUsersValidated(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        var validationBonuses = validationBonusRepository.findAllByBonusId(projectId);
        return ListUtil.map(validationBonuses, ValidationBonus::author);
    }

    public void createValidationBonus(String token, ValidationBonus validationBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        validationBonus.author(userService.getUserById(token, validationBonus.authorId()));
        validationBonusRepository.save(validationBonus);
    }

    public void deleteValidationBonusByAuthorId(String token, Integer bonusId, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        validationBonusRepository.deleteByAuthorIdAndBonusId(authorId, bonusId);
    }

}