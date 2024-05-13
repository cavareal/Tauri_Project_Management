package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Nested
class PresentationOrderServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private PresentationOrderRepository presentationOrderRepository;


    @InjectMocks
    private PresentationOrderService presentationOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    void getPresentationOrderByIdShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPresentationOrder")).thenReturn(false);

        assertThrows(SecurityException.class, () -> presentationOrderService.getPresentationOrderById(token, id));
    }

    @Test
    void getPresentationOrderByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readPresentationOrder")).thenReturn(true);
        when(presentationOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> presentationOrderService.getPresentationOrderById(token, id));
    }

    @Test
    void getAllPresentationOrdersByProjectShouldReturnOrdersWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;
        List<PresentationOrder> presentationOrders = Arrays.asList(new PresentationOrder(), new PresentationOrder());

        when(authService.checkAuth(token, "readPresentationOrders")).thenReturn(true);
        when(presentationOrderRepository.findAllByProject(projectId)).thenReturn(presentationOrders);

        List<PresentationOrder> result = presentationOrderService.getAllPresentationOrdersByProject(token, projectId);

        assertEquals(presentationOrders, result);
    }

    @Test
    void getAllPresentationOrdersByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readPresentationOrders")).thenReturn(false);

        assertThrows(SecurityException.class, () -> presentationOrderService.getAllPresentationOrdersByProject(token, projectId));
    }

    @Test
    void createPresentationOrderShouldSaveOrderWhenAuthorized() {
        String token = "validToken";
        PresentationOrder presentationOrder = new PresentationOrder();

        when(authService.checkAuth(token, "addPresentationOrder")).thenReturn(true);

        presentationOrderService.createPresentationOrder(token, presentationOrder);

        verify(presentationOrderRepository, times(1)).save(presentationOrder);
    }

    @Test
    void createPresentationOrderShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        PresentationOrder presentationOrder = new PresentationOrder();

        when(authService.checkAuth(token, "addPresentationOrder")).thenReturn(false);

        assertThrows(SecurityException.class, () -> presentationOrderService.createPresentationOrder(token, presentationOrder));
    }

    @Test
    void deletePresentationOrderShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deletePresentationOrder")).thenReturn(false);

        assertThrows(SecurityException.class, () -> presentationOrderService.deletePresentationOrder(token, id));
    }

    @Test
    void deleteAllPresentationOrdersByProjectShouldDeleteOrdersWhenAuthorizedAndProjectExists() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deletePresentationOrder")).thenReturn(true);

        presentationOrderService.deleteAllPresentationOrdersByProject(token, projectId);

        verify(presentationOrderRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllPresentationOrdersByProjectShouldThrowSecurityExceptionWhenUnauthorized() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deletePresentationOrder")).thenReturn(false);

        assertThrows(SecurityException.class, () -> presentationOrderService.deleteAllPresentationOrdersByProject(token, projectId));
    }

}
