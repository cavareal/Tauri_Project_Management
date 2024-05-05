package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationFlagService {

    private final AuthService authService;
    private final ValidationFlagRepository validationFlagRepository;

    public ValidationFlag getValidationFlagByAuthorId(String token, Integer flagid, Integer authorId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findByAuthorIdAndFlagId(authorId, flagid);
    }

    public List<ValidationFlag> getAllValidationFlags(String token, Integer flagId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlags"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findAllByFlag(flagId);
    }

    //TODO Creer tout les validations flags associé au flag passé en paramètre
    public void createValidationFlags(String token, ValidationFlag validationFlag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationFlagRepository.save(validationFlag);
    }

    public void updateValidationFlag(String token, Integer flagId, Integer authorId, ValidationFlag updatedValidationFlag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        ValidationFlag validationFlag = getValidationFlagByAuthorId(token, flagId, authorId);

        if (updatedValidationFlag.confirmed() != null) validationFlag.confirmed(updatedValidationFlag.confirmed());
        validationFlagRepository.save(validationFlag);
    }

}