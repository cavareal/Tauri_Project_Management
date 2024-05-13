package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.service.GradeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grades")
@Tag(name = "grades")
public class GradeController {

    private final GradeService gradeService;
    private final ResponseMessage responseMessage = new ResponseMessage("grade");
    private final GradeTypeRepository gradeTypeRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Grade grade = gradeService.getGradeById(token, id);
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/unimported")
    public ResponseEntity<List<Grade>> getAllUnimportedGradesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Grade> grades = gradeService.getAllUnimportedGradesByProject(token, projectId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/imported")
    public ResponseEntity<List<Grade>> getAllImportedGradesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Grade> importedGrades = gradeService.getAllImportedGradesByProject(token, projectId);
        return ResponseEntity.ok(importedGrades);
    }

    @PostMapping
    public ResponseEntity<String> createGrade(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Grade grade) {
        gradeService.createGrade(token, grade);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGrade(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody Grade updatedGrade) {
        gradeService.updateGrade(token, id, updatedGrade);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrade(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        gradeService.deleteGrade(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllGradesByProject(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        gradeService.deleteAllGradesByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }

    /**
     * This method is a POST endpoint that accepts a JSON string representing a map of evaluations.
     *
     * @param evaluations A JSON string representing a map of evaluations.
     * @return A ResponseEntity with either a success message or an error message.
     */
	/*@PostMapping("/add-grade-to-team/{userId}")
	public ResponseEntity<Map<String, String>> addGradeFromArray(@RequestBody String evaluations, @PathVariable Integer userId) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<String, List<Map<String, Object>>>> typeReference = new TypeReference<>() {
		};
		Map<String, List<Map<String, Object>>> evaluationsMap = objectMapper.readValue(evaluations, typeReference);

		for (Map.Entry<String, List<Map<String, Object>>> entry : evaluationsMap.entrySet()) {
			String teamName = entry.getKey();
			List<Map<String, Object>> evaluationsList = entry.getValue();

			for (Map<String, Object> evaluation : evaluationsList) {
				if (evaluation.get("gradeType").equals("gradeOverallPerformance")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Performance Globale", userId);
				}
				if (evaluation.get("gradeType").equals("gradeMaterialSupport")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Support Matériel", userId);
				}
				if (evaluation.get("gradeType").equals("gradeContentPresentation")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Contenu de la présentation", userId);
				}
				if (evaluation.get("gradeType").equals("gradeTechnicalSolution")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Solution technique", userId);
				}
				if (evaluation.get("gradeType").equals("gradeProjectManagement")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Gestion de projet", userId);
				}
				if (evaluation.get("gradeType").equals("gradeSprintConformity")) {
					Integer value = (Integer) evaluation.get("grade");
					gradeService.assignGradeToTeam(teamName, value, "Conformité au sprint", userId);
				}
			}
		}
		return ResponseEntity.ok(Map.of("message", "Grades added successfully."));
	}*/

    //TODO : Handle the token
    //@GetMapping("/unimported/averages") => On peut récup le user dans le token ?
    @GetMapping("/average-grades-by-grade-type-by-role/{userId}")
    public ResponseEntity<List<List<Double>>> getAverageGradesByGradeTypeByRole(@RequestHeader("Authorization") String token, @PathVariable Integer userId) {
        ArrayList<List<Double>> gradeByTypes = new ArrayList<>();
        ArrayList<Double> gradeByRoles;

        for (GradeType gradeType : gradeTypeRepository.findAllForGroup()) {
            gradeByRoles = new ArrayList<>();
            for (RoleType roleType : RoleType.values()) {
                double grade = getAverageGradeByRoleType(userId, roleType, gradeType.name());
                gradeByRoles.add(grade);
            }
            gradeByTypes.add(gradeByRoles);
        }

        if (gradeByTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(gradeByTypes);
    }

    /**
     * Helper method
     */
    private double getAverageGradeByRoleType(Integer userId, RoleType roleType, String gradeType) {
        try {
            return gradeService.getAverageGradesByGradeTypeByRoleType(userId, roleType, gradeType);
        } catch (NullPointerException e) {
            return -1.0;
        }
    }

    @GetMapping("download-student-individual-grades/{projectId}")
    public ResponseEntity<byte[]> downloadStudentGrades(@RequestHeader("Authorization") String token, @PathVariable Integer projectId) throws IOException {
        byte[] gradesCSV = gradeService.createStudentIndividualGradesCSVReport(token, projectId);
        return ResponseEntity.ok(gradesCSV);
    }


}
