package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Bonus;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.BonusRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final BonusRepository bonusRepository;
    private final ValidationBonusService validationBonusService;
    private final UserService userService;
    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;

    /**
     * Get a bonus by its id
     * @param id the id of the bonus
     * @return the bonus
     */
    public Bonus getBonusById(Integer id) {
        return bonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bonus", id));
    }

    /**
     * Get all bonuses by project
     * @param projectId the id of the project
     * @return the list of bonuses
     */
    public List<Bonus> getAllBonusesByProject(Integer projectId) {
        return bonusRepository.findAllByProject(projectId);
    }

    /**
     * Create a bonus
     * @param bonus the bonus to create
     */
    public void createBonus(Bonus bonus) {
        bonusRepository.save(bonus);
    }

    /**
     * Update a bonus
     * @param id the id of the bonus
     * @param updatedBonus the updated bonus
     */
    public void updateBonus(Integer id, Bonus updatedBonus) {
        Bonus bonus = getBonusById(id);

        boolean isLimited = bonus.limited();
        if(isLimited && Math.abs(updatedBonus.value()) > 4) {
            throw new IllegalArgumentException("The value of a limited bonus must be between -4 and 4");
        }

        bonus.value(updatedBonus.value());
        if(updatedBonus.authorId() != null) bonus.author(userService.getUserById(updatedBonus.authorId()));

        if (updatedBonus.comment() != null) bonus.comment(updatedBonus.comment());

        bonusRepository.save(bonus);

        if(isLimited) validationBonusService.deleteAllValidationBonuses(id);
    }

    /**
     * Delete a bonus with its id
     * @param id the id of the bonus
     */
    public void deleteBonus(Integer id) {
        getBonusById(id);
        bonusRepository.deleteById(id);
    }

    /**
     * Delete all bonuses by project
     * @param projectId the id of the project
     */
    public void deleteAllBonusesByProject(Integer projectId) {
        bonusRepository.deleteAllByProject(projectId);
    }


    public List<Bonus> getValidationBonusesByTeam(Integer teamId) {
        List <Bonus> bonuses = new ArrayList<>();
        List <Student> students = studentRepository.findByTeam(teamId);
        // Students bonuses
        for(Student student : students) {
            User user = userService.getUserById(student.id());
            Bonus bonus = bonusRepository.findAllByAuthorId(user.id());
            if (bonus != null){
                bonuses.add(bonus);
            }
        }

        // SS bonus
        User leader = teamRepository.findLeaderByTeamId(teamId);
        Bonus leaderBonus = bonusRepository.findAllByAuthorId(leader.id());
        if (leaderBonus != null){
            bonuses.add(leaderBonus);
        }

        return bonuses;
    }

}
