package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.service.FlagService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import fr.eseo.tauri.repository.FlagRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.FlagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flags")
@Tag(name = "flags")
public class FlagController {

    private final FlagService flagService;
    private final ResponseMessage responseMessage = new ResponseMessage("flag");
	private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<Flag> getFlagById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Flag flag = flagService.getFlagById(token, id);
        return ResponseEntity.ok(flag);
    }

    @GetMapping
    public ResponseEntity<List<Flag>> getAllFlagsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        List<Flag> flags = flagService.getAllFlagsByProject(token, projectId);
        return ResponseEntity.ok(flags);
    }

    @PostMapping
    public ResponseEntity<String> createFlag(@RequestHeader("Authorization") String token, @Validated(Create.class) @RequestBody Flag flag) {
        flagService.createFlag(token, flag);
        CustomLogger.info(responseMessage.create());
        return ResponseEntity.ok(responseMessage.create());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class)@RequestBody Flag updatedFlag) {
        flagService.updateFlag(token, id, updatedFlag);
        CustomLogger.info(responseMessage.update());
        return ResponseEntity.ok(responseMessage.update());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlag(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        flagService.deleteFlag(token, id);
        CustomLogger.info(responseMessage.delete());
        return ResponseEntity.ok(responseMessage.delete());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllFlagsByProject(@RequestHeader("Authorization") String token, @RequestParam Integer projectId) {
        flagService.deleteAllFlagsByProject(token, projectId);
        CustomLogger.info(responseMessage.deleteAllFromCurrentProject());
        return ResponseEntity.ok(responseMessage.deleteAllFromCurrentProject());
    }

	// TODO: Refactor this method
	@GetMapping("/author/{authorId}/description/{description}")
	public ResponseEntity<List<Flag>> getFlagsByAuthorAndDescription(@PathVariable Integer authorId, @PathVariable String description, @RequestHeader("Authorization") String token){
		String permission = "read Flag";
		if (Boolean.TRUE.equals(authService.checkAuth(token, permission))) {
			try{
				return ResponseEntity.status(HttpStatus.OK).body(flagService.getFlagsByAuthorAndDescription(authorId, description));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}