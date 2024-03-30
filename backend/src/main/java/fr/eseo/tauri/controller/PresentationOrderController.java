package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.repository.PresentationOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presentation_orders")
public class PresentationOrderController {

    //@Autowired

    private final PresentationOrderRepository presentationOrderRepository;

    public PresentationOrderController(PresentationOrderRepository presentationOrderRepository) {
        this.presentationOrderRepository = presentationOrderRepository;
    }

    @PostMapping("/add")
    public PresentationOrder addPresentationOrder(@RequestBody PresentationOrder presentationOrder) {
        return presentationOrderRepository.save(presentationOrder);
    }

    @GetMapping("/all")
    public Iterable<PresentationOrder> getAllPresentationOrders() {
        return presentationOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public PresentationOrder getPresentationOrderById(@PathVariable Integer id) {
        return presentationOrderRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public PresentationOrder updatePresentationOrder(@PathVariable Integer id, @RequestBody PresentationOrder presentationOrderDetails) {
        PresentationOrder presentationOrder = presentationOrderRepository.findById(id).orElse(null);
        if (presentationOrder != null) {
            presentationOrder.value(presentationOrderDetails.value());
            return presentationOrderRepository.save(presentationOrder);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deletePresentationOrder(@PathVariable Integer id) {
        presentationOrderRepository.deleteById(id);
        return "PresentationOrder deleted";
    }
}
