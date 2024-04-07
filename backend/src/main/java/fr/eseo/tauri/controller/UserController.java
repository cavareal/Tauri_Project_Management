package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.service.RoleService;
import fr.eseo.tauri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;
	private final RoleService roleService;

	@Autowired
	public UserController(UserRepository userRepository, UserService userService, RoleService roleService) {
		this.userRepository = userRepository;
        this.userService = userService;
		this.roleService = roleService;
    }

	@PostMapping(path = "/")
	public @ResponseBody String addUser(@RequestParam String name, @RequestParam String email) {
		User user = new User();
		user.name(name);
		user.email(email);
		userRepository.save(user);
		return "Saved";
	}

	@GetMapping(path = "/")
	public @ResponseBody Iterable<User> allUsers() {
		return userRepository.findAll();
	}

	@GetMapping(path = "/roles/{roleType}")
	public @ResponseBody Iterable<User> getUsersByRole(@PathVariable RoleType roleType) {
		return roleService.getUsersByRoleType(roleType);
	}

	@GetMapping(path = "/{id}")
	public @ResponseBody User getUser(@PathVariable Integer id) {
		return userRepository.findById(id).orElse(null);
	}

	@GetMapping(path = "/{id}/hasPermission")
	public @ResponseBody Boolean hasPermission(@PathVariable Integer id, @RequestParam PermissionType permissionRequired) {
		return userService.hasPermission(id, permissionRequired);
	}

	@PutMapping(path = "/update")
	public @ResponseBody String updateUser(@RequestParam Integer id, @RequestParam String name, @RequestParam String email) {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			user.name(name);
			user.email(email);
			userRepository.save(user);
			return "Updated";
		}
		return "User not found";
	}

 	@DeleteMapping(path = "/delete/{id}")
	public @ResponseBody String deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
		return "Deleted";
	}

}