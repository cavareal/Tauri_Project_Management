package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.model.enumeration.FlagType;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.FlagRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.validator.flag.CreateFlagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final AuthService authService;
    private final FlagRepository flagRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public List<Flag> getAllFlags(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readFlags"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return flagRepository.findAll();
    }

    public Flag getFlagById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return flagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("flag", id));
    }

    public void addFlag(String token, CreateFlagValidator flagDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int flagsNumber = flagRepository.findAll().size();
            //flagRepository.save(flag);
            if(flagRepository.findAll().size() == flagsNumber){
            //    throw new DataAccessException("Error : Could not add flag created by " + flag.author().name()) {};
        }
    }

    public void updateFlag(String token, Integer id, Map<String, Object> flagDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateFlag"))) {
            Flag flag = getFlagById(token, id);

            for (Map.Entry<String, Object> entry : flagDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "description":
                        flag.description((String) value);
                        break;
                    case "type":
                        flag.type(FlagType.valueOf((String) value));
                        break;
                    case "firstStudent":
                        Map<String, Object> firstStudentMap = (Map<String, Object>) value;
                        flag.firstStudent(studentRepository.findById((Integer) firstStudentMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("student", (Integer) firstStudentMap.get("id"))));
                        break;
                    case "secondStudent":
                        Map<String, Object> secondStudentMap = (Map<String, Object>) value;
                        flag.secondStudent(studentRepository.findById((Integer) secondStudentMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("student", (Integer) secondStudentMap.get("id"))));
                        break;
                    case "author":
                        Map<String, Object> userMap = (Map<String, Object>) value;
                        flag.author(userRepository.findById((Integer) userMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userMap.get("id"))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
            flagRepository.save(flag);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllFlags(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        flagRepository.deleteAll();
        if(!flagRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all flags") {};
        }
    }

    public void deleteFlag(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteFlag"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getFlagById(token, id);
        int flagsNumber = flagRepository.findAll().size();
        flagRepository.deleteById(id);
        if(flagRepository.findAll().size() == flagsNumber){
            throw new DataAccessException("Error : Could not delete flag with id : " + id) {};
        }
    }
}