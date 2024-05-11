package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.security.AuthRequest;
import fr.eseo.tauri.security.AuthResponse;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.eseo.tauri.security.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("request : " + request);

            User userDetails = (User) userDetailsService.loadUserByUsername(request.login());
            if (passwordEncoder.matches(request.password(), userDetails.getPassword())) {
                CustomLogger.info("Credentials match");
                String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
                CustomLogger.info("Token generated");
                AuthResponse response = new AuthResponse(userDetails.getUsername(), accessToken);
                CustomLogger.info("Response ready");
                System.out.println("Response : " + response);
                return ResponseEntity.ok(response);
            }
            System.out.println("Credentials doesn't match");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
