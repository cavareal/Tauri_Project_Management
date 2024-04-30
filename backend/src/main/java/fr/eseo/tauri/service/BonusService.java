package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final AuthService authService;
    private final BonusRepository bonusRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public List<Bonus> getAllBonuses(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findAll();
    }

    public Bonus getBonusById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bonus", id));
    }


    public void addBonuses(String token, List<Bonus> bonuses) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int bonusesNumber = bonusRepository.findAll().size();
        for(Bonus bonus : bonuses) {
            bonusRepository.save(bonus);
            if(bonusRepository.findAll().size() == bonusesNumber){
                throw new DataAccessException("Error : Could not add bonus attributed by " + bonus.author().name() + " to " + bonus.student().name()) {};
            } else {
                bonusesNumber++;
            }
        }
    }

    public void updateBonus(String token, Integer id, Map<String, Object> bonusDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateBonus"))) {
            Bonus bonus = getBonusById(token, id);

            for (Map.Entry<String, Object> entry : bonusDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "value":
                        bonus.value(Float.parseFloat((String) value));
                        break;
                    case "comment":
                        bonus.comment((String) value);
                        break;
                    case "limited":
                        bonus.limited((Boolean) value);
                        break;
                    case "sprint": //à changer si on change le schéma des clés étrangères dans le front pour n'envoyer que l'id de l'objet et pas l'objet tout entier
                        Map<String, Object> sprintMap = (Map<String, Object>) value;
                        bonus.sprint(sprintRepository.findById((Integer) sprintMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("sprint", (Integer) sprintMap.get("id"))));
                        break;
                    case "author":
                        Map<String, Object> userMap = (Map<String, Object>) value;
                        bonus.author(userRepository.findById((Integer) userMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userMap.get("id"))));
                        break;
                    case "student":
                        Map<String, Object> studentMap = (Map<String, Object>) value;
                        bonus.student(studentRepository.findById((Integer) studentMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("student", (Integer) studentMap.get("id"))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
            bonusRepository.save(bonus);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllBonuses(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        bonusRepository.deleteAll();
        if(!bonusRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all bonuses") {};
        }
    }

    public void deleteBonus(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getBonusById(token, id);
        int bonusesNumber = bonusRepository.findAll().size();
        bonusRepository.deleteById(id);
        if(bonusRepository.findAll().size() == bonusesNumber){
            throw new DataAccessException("Error : Could not delete bonus with id : " + id) {};
        }
    }

}
