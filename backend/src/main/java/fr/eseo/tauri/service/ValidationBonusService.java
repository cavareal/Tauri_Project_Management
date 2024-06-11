package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.ValidationBonus;
import fr.eseo.tauri.repository.ValidationBonusRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationBonusService {

    private final ValidationBonusRepository validationBonusRepository;
    private final UserService userService;
    private final StudentService studentService;
    @Lazy
    private final BonusService bonusService;

    public ValidationBonus getValidationBonusByAuthorId(Integer bonusId, Integer authorId) {
        return validationBonusRepository.findByAuthorIdAndBonusId(bonusId, authorId);
    }

    public List<ValidationBonus> getAllValidationBonuses(Integer bonusId) {
        return validationBonusRepository.findAllByBonusId(bonusId);
    }

    public void createValidationBonus(ValidationBonus validationBonus) {
        validationBonus.bonus(bonusService.getBonusById(validationBonus.bonusId()));
        validationBonus.author(userService.getUserById(validationBonus.authorId()));

        validationBonusRepository.save(validationBonus);
    }

    public void deleteAllValidationBonuses(Integer bonusId) {
        validationBonusRepository.deleteAllByBonusId(bonusId);

    }


    public List<Bonus> getStudentValidationBonusesByTeam(Integer teamId) {
        List<Student> students = studentRepository.findAllByTeamId(teamId);
        List <Bonus> bonuses = null;

        for(Student student : students) {
            User user = userService.getUserById(student.id());
            CustomLogger.info("User auth: " + user);
            bonuses.add(bonusRepository.findAllByAuthorId(user.id()));
        }
        CustomLogger.info("Get all bonuses by team : " + bonuses);
        return bonuses;
    }

}