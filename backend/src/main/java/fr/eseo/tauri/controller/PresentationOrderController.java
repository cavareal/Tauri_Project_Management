package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentation-orders")
@Tag(name = "presentation-orders")
public class PresentationOrderController {

    private final PresentationOrderRepository presentationOrderRepository;

    @PostMapping("/")
    public PresentationOrder addPresentationOrder(@RequestBody PresentationOrder presentationOrder) {
        return presentationOrderRepository.save(presentationOrder);
    }

    @GetMapping("/")
    public Iterable<PresentationOrder> getAllPresentationOrders() {
        return presentationOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public PresentationOrder getPresentationOrderById(@PathVariable Integer id) {
        return presentationOrderRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public PresentationOrder updatePresentationOrder(@PathVariable Integer id, @RequestBody PresentationOrder presentationOrderDetails) {
        PresentationOrder presentationOrder = presentationOrderRepository.findById(id).orElse(null);
        if (presentationOrder != null) {
            presentationOrder.value(presentationOrderDetails.value());
            return presentationOrderRepository.save(presentationOrder);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deletePresentationOrder(@PathVariable Integer id) {
        presentationOrderRepository.deleteById(id);
        return "PresentationOrder deleted";
    }
}
