package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.PresentationOrderService;
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
    private PresentationOrderRepository presentationOrderRepository;


    @InjectMocks
    private PresentationOrderService presentationOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPresentationOrderByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Integer id = 1;

        when(presentationOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> presentationOrderService.getPresentationOrderById(id));
    }

    @Test
    void getAllPresentationOrdersByProjectShouldReturnOrdersWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;
        List<PresentationOrder> presentationOrders = Arrays.asList(new PresentationOrder(), new PresentationOrder());

        when(presentationOrderRepository.findAllByProject(projectId)).thenReturn(presentationOrders);

        List<PresentationOrder> result = presentationOrderService.getAllPresentationOrdersByProject(projectId);

        assertEquals(presentationOrders, result);
    }

    @Test
    void createPresentationOrderShouldSaveOrderWhenAuthorized() {
        PresentationOrder presentationOrder = new PresentationOrder();


        presentationOrderService.createPresentationOrder(presentationOrder);

        verify(presentationOrderRepository, times(1)).save(presentationOrder);
    }

    @Test
    void deleteAllPresentationOrdersByProjectShouldDeleteOrdersWhenAuthorizedAndProjectExists() {
        Integer projectId = 1;

        presentationOrderService.deleteAllPresentationOrdersByProject(projectId);

        verify(presentationOrderRepository, times(1)).deleteAllByProject(projectId);
    }

}
