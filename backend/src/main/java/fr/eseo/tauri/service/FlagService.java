package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final AuthService authService;
    private final FlagRepository flagRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final ProjectService projectService;

    public Flag getFlagById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return flagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("flag", id));
    }

    public List<Flag> getAllFlagsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readFlags"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return flagRepository.findAllByProject(projectId);
    }

    public void createFlag(String token, Flag flag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        flag.author(userService.getUserById(token, flag.authorId()));
        flag.firstStudent(studentService.getStudentById(token, flag.firstStudentId()));
        flag.secondStudent(studentService.getStudentById(token, flag.secondStudentId()));

        flagRepository.save(flag);
    }

    public void updateFlag(String token, Integer id, Flag updatedFlag) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Flag flag = getFlagById(token, id);

        if (updatedFlag.description() != null) flag.description(updatedFlag.description());
        if (updatedFlag.type() != null) flag.type(updatedFlag.type());
        if (updatedFlag.status() != null) flag.status(updatedFlag.status());
        if (updatedFlag.firstStudentId() != null) flag.firstStudent(studentService.getStudentById(token, updatedFlag.firstStudentId()));
        if (updatedFlag.secondStudentId() != null) flag.secondStudent(studentService.getStudentById(token, updatedFlag.secondStudentId()));
        if (updatedFlag.authorId() != null) flag.author(userService.getUserById(token, updatedFlag.authorId()));
        if (updatedFlag.projectId() != null) flag.project(projectService.getProjectById(token, updatedFlag.projectId()));

        flagRepository.save(flag);
    }

    public void deleteFlag(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getFlagById(token, id);
        flagRepository.deleteById(id);
    }

    public void deleteAllFlagsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        flagRepository.deleteAllByProject(projectId);
    }

	// TODO: Refactor this method
	public List<Flag> getFlagsByAuthorAndDescription(Integer authorId , String description) {
		return flagRepository.findByAuthorIdAndDescription(authorId, description);
	}
}