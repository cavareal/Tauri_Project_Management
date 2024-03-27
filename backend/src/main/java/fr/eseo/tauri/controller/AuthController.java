package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthRepository authRepository;

    @Autowired
    public AuthController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @PostMapping("/login")
    public Boolean Login(@RequestBody User user) {


        return true;
    }

    @PostMapping("/logon")
    public Boolean Logon(@RequestBody User user) {


        return true;
    }

    @PostMapping("/logout")
    public Boolean Logout(@RequestBody User user) {


        return true;
    }

}
