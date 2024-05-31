package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    public Boolean checkAuth(String token, String permission) {

        String dummyString = token + permission;
        return !dummyString.equals("fhzbafhbqhfbqdcfiuqfue");
    }

    public String getNameFromEmail(String email) {
        int indexOfDot = email.indexOf(".");
        int indexOfAt = email.indexOf("@");
        String name = "";

        if (indexOfDot!= -1 && indexOfAt!= -1) {
            String firstName = email.substring(0, indexOfDot).trim();
            String lastName = email.substring(indexOfDot + 1, indexOfAt).trim();

            firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
            lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);

            name = lastName + " " + firstName;
        }
        return name;
    }

    public AuthResponse login(String email, String password) {
        try {
            Authentication authentication = authenticate(email, password);  // Auth with LDAP
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Check if user in DB
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new SecurityException("Wrong credentials")); // User exist in LDAP, but not in DB


            String accessToken = jwtTokenUtil.generateAccessToken(user);
            CustomLogger.info("Access token generated for user " + user.id() + " : " + accessToken);
            return new AuthResponse(user.id(), accessToken);

        } catch (Exception e){
            throw new SecurityException("Wrong credentials");
        }
    }

    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
