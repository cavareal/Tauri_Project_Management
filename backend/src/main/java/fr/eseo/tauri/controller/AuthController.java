package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.AuthRequest;
import fr.eseo.tauri.model.LoginResponse;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "auth")
public class AuthController {

    @PostMapping("/login")
    public Boolean login(@RequestBody User user) {
        CustomLogger.info(user.email() + " is trying to log in");

            // Prevent XSS faille
            String loginSecure = HtmlUtils.htmlEscape(request.get("username"));
            String passwordSecure = HtmlUtils.htmlEscape(request.get("password"));

            // TODO faille xss : htmlEscape and/or validator, when receiving username and password
            // TODO implement logic to authenticate user, and return data to user (adapt type of var response)

            String response = "";
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
