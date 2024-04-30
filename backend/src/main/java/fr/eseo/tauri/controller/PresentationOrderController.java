package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.PresentationOrder;
import fr.eseo.tauri.service.PresentationOrderService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentations-order")
@Tag(name = "presentations-order")
public class PresentationOrderController {

    private final PresentationOrderService presentationOrderService;

    @GetMapping
    public ResponseEntity<List<PresentationOrder>> getAllPresentationOrders(@RequestHeader("Authorization") String token) {
        List<PresentationOrder> presentationOrders = presentationOrderService.getAllPresentationOrders(token);
        return ResponseEntity.ok(presentationOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentationOrder> getPresentationOrderById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        PresentationOrder presentationOrder = presentationOrderService.getPresentationOrderById(token, id);
        return ResponseEntity.ok(presentationOrder);
    }

    @PostMapping
    public ResponseEntity<String> addPresentationOrders(@RequestHeader("Authorization") String token, @RequestBody List<PresentationOrder> presentationOrders) {
        presentationOrderService.addPresentationOrders(token, presentationOrders);
        CustomLogger.info("The presentation order(s) have been added");
        return ResponseEntity.ok("The presentation order(s) have been added");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePresentationOrder(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        presentationOrderService.updatePresentationOrder(token, id, request);
        CustomLogger.info("The presentation order has been updated");
        return ResponseEntity.ok("The presentation order has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPresentationOrders(@RequestHeader("Authorization") String token) {
        presentationOrderService.deleteAllPresentationOrders(token);
        CustomLogger.info("All the presentation orders have been deleted");
        return ResponseEntity.ok("All the presentation orders have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePresentationOrder(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        presentationOrderService.deletePresentationOrder(token, id);
        CustomLogger.info("The presentation order has been deleted");
        return ResponseEntity.ok("The presentation order has been deleted");
    }
}
