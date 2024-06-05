package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.ValidationFlag;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationFlagService {

    private final ValidationFlagRepository validationFlagRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final TeamService teamService;

    public ValidationFlag getValidationFlagByAuthorId(Integer flagId, Integer authorId) {
        return validationFlagRepository.findByAuthorIdAndFlagId(flagId, authorId);
    }

    public List<ValidationFlag> getAllValidationFlags(Integer flagId) {
        return validationFlagRepository.findAllByFlag(flagId);
    }

    public void createValidationFlags(Flag flag) {
        ValidationFlag validationFlagPL = new ValidationFlag();
        validationFlagPL.flag(flag);
        validationFlagPL.author(roleService.getUsersByRoleType("token", RoleType.PROJECT_LEADER).get(0));
        validationFlagRepository.save(validationFlagPL);

        if(userService.getRolesByUserId(flag.author().id()).contains(RoleType.OPTION_STUDENT)){
            List<Student> students = teamService.getStudentsByTeamId("token", flag.firstStudent().team().id());
            students.addAll(teamService.getStudentsByTeamId("token", flag.secondStudent().team().id()));
            for(Student student: students){
                ValidationFlag validationFlag = new ValidationFlag();
                validationFlag.flag(flag);
                validationFlag.author(student);
                validationFlagRepository.save(validationFlag);
            }
        }
    }

    public void updateValidationFlag(Integer flagId, Integer authorId, ValidationFlag updatedValidationFlag) {
        ValidationFlag validationFlag = getValidationFlagByAuthorId(flagId, authorId);

        if (updatedValidationFlag.confirmed() != null) validationFlag.confirmed(updatedValidationFlag.confirmed());
        validationFlagRepository.save(validationFlag);
    }

}