package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentationOrderService {

    private final AuthService authService;
    private final PresentationOrderRepository presentationOrderRepository;

    public PresentationOrder getPresentationOrderById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return presentationOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("presentationOrder", id));
    }

    public List<PresentationOrder> getAllPresentationOrdersByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPresentationOrders"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return presentationOrderRepository.findAllByProject(projectId);
    }

    public void createPresentationOrder(String token, PresentationOrder presentationOrder) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addPresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        presentationOrderRepository.save(presentationOrder);
    }

    public void updatePresentationOrder(String token, Integer id, PresentationOrder updatedPresentationOrder) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updatePresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        PresentationOrder presentationOrder = getPresentationOrderById(token, id);

        if (updatedPresentationOrder.value() != null) presentationOrder.value(updatedPresentationOrder.value());

        presentationOrderRepository.save(presentationOrder);
    }

    public void deletePresentationOrder(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getPresentationOrderById(token, id);
        presentationOrderRepository.deleteById(id);
    }

    public void deleteAllPresentationOrdersByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deletePresentationOrder"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        presentationOrderRepository.deleteAllByProject(projectId);
    }

    public List<PresentationOrder> getPresentationOrderByTeamIdAndSprintId(String token, Integer teamId, Integer sprintId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPresentationOrders"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return presentationOrderRepository.findAllByTeamIdAndSprintId(teamId, sprintId);
    }

}