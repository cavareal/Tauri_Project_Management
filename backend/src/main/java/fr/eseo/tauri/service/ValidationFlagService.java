package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.model.id_class.ValidationFlagId;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidationFlagService {

    private final AuthService authService;
    private final ValidationFlagRepository validationFlagRepository;

    public List<ValidationFlag> getAllValidationFlags(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlags"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findAll();
    }

    /*public ValidationFlag getValidationFlagById(String token, ValidationFlagId id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationFlagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("validationFlag", id));
    }*/

    public void addValidationFlags(String token, List<ValidationFlag> validationFlags) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int validationFlagsNumber = validationFlagRepository.findAll().size();
        for(ValidationFlag validationFlag : validationFlags) {
            validationFlagRepository.save(validationFlag);
            if(validationFlagRepository.findAll().size() == validationFlagsNumber){
                throw new DataAccessException("Error : Could not add validation flag") {};
            } else {
                validationFlagsNumber++;
            }
        }
    }

    public void updateValidationFlag(String token, ValidationFlagId id, Map<String, Object> validationFlagDetails) {
        /*if (Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationFlag"))) {
            ValidationFlag validationFlag = getValidationFlagById(token, id);

            for (Map.Entry<String, Object> entry : validationFlagDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "confirmed":
                        validationFlag.confirmed((Boolean) value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }

            validationFlagRepository.save(validationFlag);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }*/
    }

    public void deleteAllValidationFlags(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationFlagRepository.deleteAll();
        if(!validationFlagRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all validation flags") {};
        }
    }

    public void deleteValidationFlag(String token, ValidationFlagId id) {
        /*if (Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getValidationFlagById(token, id);
        int validationFlagsNumber = validationFlagRepository.findAll().size();
        validationFlagRepository.deleteById(id);
        if(validationFlagRepository.findAll().size() == validationFlagsNumber){
            throw new DataAccessException("Error : Could not delete validation flag with id : " + id) {};
        }*/
    }
}