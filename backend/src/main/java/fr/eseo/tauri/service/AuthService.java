package fr.eseo.tauri.service;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.security.AuthResponse;
import fr.eseo.tauri.security.JwtTokenUtil;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public Boolean checkAuth(String token, String permission) {
        CustomLogger.info("Checking if user's token can do this request" + token + " " + permission);
        // Check if user's token can do this request
        //Use the hasPermission method
        // We need to set the

        return true;
    }

    public AuthResponse login(String login, String password) {

        User user = (User) userDetailsService.loadUserByUsername(login);
        if (passwordEncoder.matches(password, user.getPassword())) {
            CustomLogger.info("Credentials match");

            String accessToken = jwtTokenUtil.generateAccessToken(user);
            CustomLogger.info("Token generated");

            AuthResponse response = new AuthResponse(user.id(), accessToken);
            CustomLogger.info("Response ready");

            System.out.println("Response : " + response);
            return response;
        }

        throw new SecurityException("Wrong credentials");
    }

}
