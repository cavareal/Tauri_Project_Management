package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "auth")
public class AuthController {

    @PostMapping("/login")
    public Boolean login(@RequestBody User user) {
        CustomLogger.info(user.email() + " is trying to log in");

        return true;
    }

    @PostMapping("/logon")
    public Boolean logon(@RequestBody User user) {
        CustomLogger.info(user.email() + " is trying to log on");

        return true;
    }

    @PostMapping("/logout")
    public Boolean logout(@RequestBody User user) {
        CustomLogger.info(user.email() + " is trying to log out");

        return true;
    }


}
