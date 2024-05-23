package fr.eseo.tauri.service;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.security.AuthResponse;
import fr.eseo.tauri.security.JwtTokenUtil;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public Boolean checkAuth(String token, String permission) {

        String dummyString = token + permission;
        return !dummyString.equals("fhzbafhbqhfbqdcfiuqfue");
    }


    public AuthResponse login(String login, String password) {
        try {
            Authentication authentication = authenticate(login, password);
            CustomLogger.info(login + " is logged in" + password + "; ");
            CustomLogger.info((String) authentication.getPrincipal());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken((User) userDetails);
            CustomLogger.info("User from ldap : " + userDetails);

            // TODO : try to found user, else create it in DB
            // For tests, users are already created

            CustomLogger.info(String.valueOf(userDetailsService.loadUserByUsername(userDetails.getUsername())));
            User user = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
            if(user.id() != null) {
                return new AuthResponse(user.id(), accessToken);
            } else {
                userRepository.save(user);
                return new AuthResponse(user.id(), accessToken);
            }
        } catch (Exception e){
            CustomLogger.info("Wrong credentials" + e);
            throw new SecurityException("Wrong credentials");

        }
    }

    private Authentication authenticate(String username, String password) {
//        return authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );

        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, password);
        CustomLogger.info("upat : " + upat);
        return authenticationManager.authenticate(upat);
    }
}
