package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationFlagService {

    private final AuthService authService;
    private final ValidationFlagRepository validationFlagRepository;
    private final UserService userService;
    private final FlagService flagService;

    public ValidationFlag getValidationFlagById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("validationFlag", id));
    }

    public List<ValidationFlag> getAllValidationFlagsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlags"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findAllByProject(projectId);
    }

    public void createValidationFlag(String token, ValidationFlag validationFlag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationFlag.author(userService.getUserById(token, validationFlag.authorId()));
        validationFlag.flag(flagService.getFlagById(token, validationFlag.flagId()));

        validationFlagRepository.save(validationFlag);
    }

    public void updateValidationFlag(String token, Integer id, ValidationFlag updatedValidationFlag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        ValidationFlag validationFlag = getValidationFlagById(token, id);

        if (updatedValidationFlag.confirmed() != null) validationFlag.confirmed(updatedValidationFlag.confirmed());
        if (updatedValidationFlag.authorId() != null) validationFlag.author(userService.getUserById(token, updatedValidationFlag.authorId()));
        if (updatedValidationFlag.flagId() != null) validationFlag.flag(flagService.getFlagById(token, updatedValidationFlag.flagId()));

        validationFlagRepository.save(validationFlag);
    }

    public void deleteValidationFlag(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getValidationFlagById(token, id);
        validationFlagRepository.deleteById(id);
    }

    public void deleteAllValidationFlagsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationFlagRepository.deleteAllByProject(projectId);
    }
}