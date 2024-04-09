package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthRepository authRepository;

    @Autowired
    public AuthController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @PostMapping("/login")
    public Boolean login(@RequestBody User user) {


        return true;
    }

    @PostMapping("/logon")
    public Boolean logon(@RequestBody User user) {


        return true;
    }

    @PostMapping("/logout")
    public Boolean logout(@RequestBody User user) {


        return true;
    }


}
