package fr.eseo.tauri.service;

import fr.eseo.tauri.controller.GlobalExceptionHandler;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PresentationOrderService {

    private final AuthService authService;
    private final PresentationOrderRepository presentationOrderRepository;
    private final StudentRepository studentRepository;
    private final SprintRepository sprintRepository;

    public List<PresentationOrder> getAllPresentationOrders(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPresentationOrders"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return presentationOrderRepository.findAll();
    }

    public PresentationOrder getPresentationOrderById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return presentationOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("presentationOrder", id));
    }

    public void addPresentationOrders(String token, List<PresentationOrder> presentationOrders) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addPresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int presentationOrdersNumber = presentationOrderRepository.findAll().size();
        for(PresentationOrder presentationOrder : presentationOrders) {
            presentationOrderRepository.save(presentationOrder);
            if(presentationOrderRepository.findAll().size() == presentationOrdersNumber){
                throw new DataAccessException("Error : Could not add presentation order") {};
            } else {
                presentationOrdersNumber++;
            }
        }
    }

    public void updatePresentationOrder(String token, Integer id, Map<String, Object> presentationOrderDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updatePresentationOrder"))) {
            PresentationOrder presentationOrder = getPresentationOrderById(token, id);

            for (Map.Entry<String, Object> entry : presentationOrderDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "value":
                        presentationOrder.value((Integer) value);
                        break;
                    case "student":
                        Map<String, Object> studentMap = (Map<String, Object>) value;
                        presentationOrder.student(studentRepository.findById((Integer) studentMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) studentMap.get("id"))));
                        break;
                    case "userFrom":
                        Map<String, Object> sprintMap = (Map<String, Object>) value;
                        presentationOrder.sprint(sprintRepository.findById((Integer) sprintMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) sprintMap.get("id"))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }

            presentationOrderRepository.save(presentationOrder);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteAllPresentationOrders(String token) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        presentationOrderRepository.deleteAll();
        if(!presentationOrderRepository.findAll().isEmpty()){
            throw new DataAccessException("Error : Could not delete all presentation orders") {};
        }
    }

    public void deletePresentationOrder(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getPresentationOrderById(token, id);
        int presentationOrdersNumber = presentationOrderRepository.findAll().size();
        presentationOrderRepository.deleteById(id);
        if(presentationOrderRepository.findAll().size() == presentationOrdersNumber){
            throw new DataAccessException("Error : Could not delete presentation order with id : " + id) {};
        }
    }
}
