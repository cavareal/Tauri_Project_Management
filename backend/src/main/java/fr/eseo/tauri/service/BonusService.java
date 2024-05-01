package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final AuthService authService;
    private final BonusRepository bonusRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public Bonus getBonusById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bonus", id));
    }

    public List<Bonus> getAllBonusesByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readBonuses"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return bonusRepository.findAllByProject(projectId);
    }

    public void createBonus(String token, Bonus bonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        if(bonus.authorId()!=null) bonus.author(userRepository.findById(bonus.authorId()).orElseThrow(() -> new ResourceNotFoundException("user", bonus.authorId())));
        if(bonus.studentId()!=null) bonus.student(studentRepository.findById(bonus.studentId()).orElseThrow(() -> new ResourceNotFoundException("student", bonus.studentId())));
        if(bonus.sprintId()!=null) bonus.sprint(sprintRepository.findById(bonus.sprintId()).orElseThrow(() -> new ResourceNotFoundException("sprint", bonus.sprintId())));
        bonusRepository.save(bonus);
    }

    public void createManyBonuses(String token, List<Bonus> bonuses) {
        for(Bonus bonus : bonuses) {
            createBonus(token, bonus);
        }
    }

    public void updateBonus(String token, Integer id, Bonus updatedBonus) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

            Bonus bonus = getBonusById(token, id);

            if (updatedBonus.value() != null) bonus.value(updatedBonus.value());
            if (updatedBonus.comment() != null) bonus.comment(updatedBonus.comment());
            if (updatedBonus.limited() != null) bonus.limited(updatedBonus.limited());
            if (updatedBonus.sprintId() != null) bonus.sprint(sprintRepository.findById(updatedBonus.sprintId()).orElseThrow(() -> new ResourceNotFoundException("sprint", updatedBonus.sprintId())));
            if (updatedBonus.authorId() != null) bonus.author(userRepository.findById(updatedBonus.authorId()).orElseThrow(() -> new ResourceNotFoundException("user", updatedBonus.authorId())));
            if (updatedBonus.studentId() != null) bonus.student(studentRepository.findById(updatedBonus.studentId()).orElseThrow(() -> new ResourceNotFoundException("student", updatedBonus.studentId())));

            bonusRepository.save(bonus);
    }

    public void deleteBonus(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getBonusById(token, id);
        bonusRepository.deleteById(id);
    }

    public void deleteAllBonuses(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteBonus"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        bonusRepository.deleteAllByProject(projectId);
    }

}
