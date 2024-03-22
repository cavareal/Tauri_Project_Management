package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.GradeScale;
import fr.eseo.tauri.repository.GradeScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade_scales")
public class GradeScaleController {

    @Autowired
    private GradeScaleRepository gradeScaleRepository;

    @PostMapping("/add")
    public GradeScale addGradeScale(@RequestBody GradeScale gradeScale) {
        return gradeScaleRepository.save(gradeScale);
    }

    @GetMapping("/all")
    public Iterable<GradeScale> getAllGradeScales() {
        return gradeScaleRepository.findAll();
    }

    @GetMapping("/{id}")
    public GradeScale getGradeScaleById(@PathVariable Integer id) {
        return gradeScaleRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public GradeScale updateGradeScale(@PathVariable Integer id, @RequestBody GradeScale gradeScaleDetails) {
        GradeScale gradeScale = gradeScaleRepository.findById(id).orElse(null);
        if (gradeScale != null) {
            gradeScale.type(gradeScaleDetails.type());
            gradeScale.content(gradeScaleDetails.content());
            return gradeScaleRepository.save(gradeScale);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGradeScale(@PathVariable Integer id) {
        gradeScaleRepository.deleteById(id);
        return "GradeScale deleted";
    }
}
