package fr.eseo.tauri.controller;


import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/")
	public @ResponseBody String addUser(@RequestParam String name, @RequestParam String email) {
		User n = new User();
		n.name(name);
		n.email(email);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path = "/")
	public @ResponseBody Iterable<User> allUsers() {
		return userRepository.findAll();
	}
}