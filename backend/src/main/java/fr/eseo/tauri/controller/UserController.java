package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.service.GradeService;
import fr.eseo.tauri.service.NotificationService;
import fr.eseo.tauri.service.RoleService;
import fr.eseo.tauri.service.UserService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ResponseMessage;
import fr.eseo.tauri.util.valid.Create;
import fr.eseo.tauri.util.valid.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
@Tag(name = "users")
public class UserController {

	private final UserService userService;
	private final ResponseMessage responseMessage = new ResponseMessage("user");
	private final RoleService roleService;
	private final GradeService gradeService;
	private final NotificationService notificationService;

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		User user = userService.getUserById(token, id);
		return ResponseEntity.ok(user);
	}

	@GetMapping(path = "/roles/{roleType}")
	public Iterable<User> getUsersByRole(@RequestHeader("Authorization") String token, @PathVariable RoleType roleType) {
		return roleService.getUsersByRoleType(token, roleType);
	}

	@PostMapping
	public ResponseEntity<String> createUser(@RequestParam String name, @Validated(Create.class) @RequestBody User user) {
		userService.createUser(name, user);
		CustomLogger.info(responseMessage.create());
		return ResponseEntity.ok(responseMessage.create());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Validated(Update.class) @RequestBody User user) {
		userService.updateUser(token, id, user);
		CustomLogger.info(responseMessage.update());
		return ResponseEntity.ok(responseMessage.update());
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteUserById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		userService.deleteUserById(token, id);
		CustomLogger.info(responseMessage.delete());
		return ResponseEntity.ok(responseMessage.delete());
	}

	@DeleteMapping
	public ResponseEntity<String> deleteAllUsers(@RequestHeader("Authorization") String token) {
		userService.deleteAllUsers(token);
		CustomLogger.info(responseMessage.deleteAll());
		return ResponseEntity.ok(responseMessage.deleteAll());
	}

	@GetMapping(path = "/{id}/permissions/{permissionType}")
	public ResponseEntity<Boolean> hasPermission(@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable PermissionType permissionType) {
		var hasPermission = userService.hasPermission(token, id, permissionType);
		return ResponseEntity.ok(hasPermission);
	}

	@GetMapping(path = "/{id}/permissions")
	public ResponseEntity<List<PermissionType>> getAllPermissions(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		var permissions = userService.getPermissionsByUser(token, id);
		return ResponseEntity.ok(permissions);
	}

	@GetMapping("{id}/team")
	public ResponseEntity<Team> getTeamByMemberId(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam("projectId") Integer projectId) {
		Team team = userService.getTeamByMemberId(token, id, projectId);
		return ResponseEntity.ok(team);
	}

	@GetMapping("{id}/roles")
	public ResponseEntity<List<RoleType>> getRolesByUserId(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
		List<RoleType> roles = userService.getRolesByUserId(token, id);
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/{userId}/notifications")
	public ResponseEntity<List<Notification>> getAllNotificationsUser(@RequestHeader("Authorization") String token, @PathVariable Integer userId) {
		List<Notification> notifications = notificationService.getNotificationsByUser(token, userId);
		return ResponseEntity.ok(notifications);
	}

}