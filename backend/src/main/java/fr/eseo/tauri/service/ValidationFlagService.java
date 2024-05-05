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

    private final AuthService authService;
    private final ValidationFlagRepository validationFlagRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final TeamService teamService;

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

    public void createValidationFlags(String token, Flag flag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addValidationFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        ValidationFlag validationFlagPL = new ValidationFlag();
        validationFlagPL.flag(flag);
        validationFlagPL.author(roleService.getUsersByRoleType(token, RoleType.PROJECT_LEADER).get(0));
        validationFlagRepository.save(validationFlagPL);

        if(userService.getRolesByUserId(token, flag.author().id()).contains(RoleType.OPTION_STUDENT)){
            List<Student> students = teamService.getStudentsByTeamId(token, flag.firstStudent().team().id());
            students.addAll(teamService.getStudentsByTeamId(token, flag.secondStudent().team().id()));
            for(Student student: students){
                ValidationFlag validationFlag = new ValidationFlag();
                validationFlag.flag(flag);
                validationFlag.author(student);
                validationFlagRepository.save(validationFlag);
            }
        }
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