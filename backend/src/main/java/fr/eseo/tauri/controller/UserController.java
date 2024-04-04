package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/users")
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
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

	@GetMapping(path = "/{id}")
	public @ResponseBody User getUser(@PathVariable Integer id) {
		return userRepository.findById(id).orElse(null);
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