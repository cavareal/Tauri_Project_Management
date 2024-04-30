package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.model.id_class.ValidationBonusId;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidationBonusService {

    private final AuthService authService;
    private final ValidationBonusRepository validationBonusRepository;

    public List<ValidationBonus> getAllValidationBonuses(String token) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonuses"))) {
            return validationBonusRepository.findAll();
        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    /*public ValidationBonus getValidationBonusById(String token, ValidationBonusId id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return validationBonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("validationBonus", id));
    }*/

    public void addValidationBonuses(String token, List<ValidationBonus> validationBonuses) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int validationBonusesNumber = validationBonusRepository.findAll().size();
        for(ValidationBonus validationBonus : validationBonuses) {
            validationBonusRepository.save(validationBonus);
            if(validationBonusRepository.findAll().size() == validationBonusesNumber){
                throw new DataAccessException("Error : Could not add validation bonus") {};
            } else {
                validationBonusesNumber++;
            }
        }
    }

    public void updateValidationBonus(String token, ValidationBonusId id, Map<String, Object> validationBonusDetails) {
        /*if (Boolean.TRUE.equals(authService.checkAuth(token, "updateValidationBonus"))) {
            ValidationBonus validationBonus = getValidationBonusById(token, id);

            for (Map.Entry<String, Object> entry : validationBonusDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "confirmed":
                        validationBonus.confirmed((Boolean) value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }

            validationBonusRepository.save(validationBonus);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }*/
    }

    public void deleteAllValidationBonuses(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        validationBonusRepository.deleteAll();
        if(!validationBonusRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all validation bonuses") {};
        }
    }

    public void deleteValidationBonus(String token, ValidationBonusId id) {
        /*if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteValidationBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getValidationBonusById(token, id);
        int validationBonusesNumber = validationBonusRepository.findAll().size();
        validationBonusRepository.deleteById(id);
        if(validationBonusRepository.findAll().size() == validationBonusesNumber){
            throw new DataAccessException("Error : Could not delete validation bonus with id : " + id) {};
        }*/
    }
}
