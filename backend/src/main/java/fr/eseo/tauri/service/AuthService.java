package fr.eseo.tauri.service;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.security.AuthResponse;
import fr.eseo.tauri.security.JwtTokenUtil;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public Boolean checkAuth(String token, String permission) {

        String dummyString = token + permission;
        return !dummyString.equals("fhzbafhbqhfbqdcfiuqfue");
    }

    public AuthResponse login(String login, String password) {
        Authentication authentication = authenticate(login, password);
        CustomLogger.info(login + " is logged in" + password + "; " + authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateAccessToken((User) userDetails);
        // TODO : change username to user id
        return new AuthResponse(userDetails.getUsername(), accessToken);
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }
}
