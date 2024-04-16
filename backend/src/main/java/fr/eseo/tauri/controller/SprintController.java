package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.repository.SprintRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sprints")
@Tag(name = "sprints")
public class SprintController {

    private final SprintRepository sprintRepository;

    public SprintController(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @PostMapping("/add")
    public Sprint addSprint(@RequestBody Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    @GetMapping("/all")
    public Iterable<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sprint getSprintById(@PathVariable Integer id) {
        return sprintRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Sprint updateSprint(@PathVariable Integer id, @RequestBody Sprint sprintDetails) {
        Sprint sprint = sprintRepository.findById(id).orElse(null);
        if (sprint != null) {
            sprint.startDate(sprintDetails.startDate());
            sprint.endDate(sprintDetails.endDate());
            sprint.endType(sprintDetails.endType());
            return sprintRepository.save(sprint);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSprint(@PathVariable Integer id) {
        sprintRepository.deleteById(id);
        return "Sprint deleted";
    }
}
