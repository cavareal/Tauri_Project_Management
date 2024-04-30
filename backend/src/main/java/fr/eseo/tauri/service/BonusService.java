package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.validator.bonus.CreateBonusValidator;
import fr.eseo.tauri.validator.bonus.UpdateBonusValidator;
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


    public void addBonuses(String token, List<CreateBonusValidator> bonusesDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int bonusesNumber = bonusRepository.findAll().size();
        Bonus bonus = new Bonus();
        for(CreateBonusValidator bonusDetails : bonusesDetails) {

            if(bonusDetails.value() != null) bonus.value(bonusDetails.value());
            if(bonusDetails.comment() != null) bonus.comment(bonusDetails.comment());
            if(bonusDetails.limited() != null) bonus.limited(bonusDetails.limited());
            if(bonusDetails.sprintId() != null) bonus.sprint(sprintRepository.findById(bonusDetails.sprintId()).orElseThrow(() -> new ResourceNotFoundException("sprint", bonusDetails.sprintId())));
            if(bonusDetails.authorId() != null) bonus.author(userRepository.findById(bonusDetails.authorId()).orElseThrow(() -> new ResourceNotFoundException("user", bonusDetails.authorId())));
            if(bonusDetails.studentId() != null) bonus.student(studentRepository.findById(bonusDetails.studentId()).orElseThrow(() -> new ResourceNotFoundException("student", bonusDetails.studentId())));

            bonusRepository.save(bonus);
            if(bonusRepository.findAll().size() == bonusesNumber){
                throw new DataAccessException("Error : Could not add bonus attributed by " + bonus.author().name() + " to " + bonus.student().name()) {};
            } else {
                bonusesNumber++;
            }
        }
    }

    public void updateBonus(String token, Integer id, UpdateBonusValidator bonusDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

            Bonus bonus = getBonusById(token, id);

            if (bonusDetails.value() != null) bonus.value(bonusDetails.value());
            if (bonusDetails.comment() != null) bonus.comment(bonusDetails.comment());
            if (bonusDetails.limited() != null) bonus.limited(bonusDetails.limited());
            if (bonusDetails.sprintId() != null) bonus.sprint(sprintRepository.findById(bonusDetails.sprintId()).orElseThrow(() -> new ResourceNotFoundException("sprint", bonusDetails.sprintId())));
            if (bonusDetails.authorId() != null) bonus.author(userRepository.findById(bonusDetails.authorId()).orElseThrow(() -> new ResourceNotFoundException("user", bonusDetails.authorId())));
            if (bonusDetails.studentId() != null) bonus.student(studentRepository.findById(bonusDetails.studentId()).orElseThrow(() -> new ResourceNotFoundException("student", bonusDetails.studentId())));

            bonusRepository.save(bonus);
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
