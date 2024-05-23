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


    public AuthResponse login(String email, String password) {
        try {
            Authentication authentication = authenticate(email, password);
            CustomLogger.info(email + " is logged in. " + authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CustomLogger.info("User from ldap : " + userDetails);

            // Check if user in DB
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseGet(() -> {
                        // Create user
                        User newUser = new User(userDetails.getUsername());
                        userRepository.save(newUser);
                        CustomLogger.info("USER CREATEEEDDD : " + newUser.id());
                        // Add role
                        var role = new Role();
                        role.user(newUser);
                        role.type(RoleType.PROJECT_LEADER);
                        roleRepository.save(role);

                        return newUser;
            });
            CustomLogger.info("User from db : " + user);

            String accessToken = jwtTokenUtil.generateAccessToken((User) userDetails);
            CustomLogger.info("Access : " + accessToken);
            return new AuthResponse(user.id(), accessToken);

        } catch (Exception e){
            CustomLogger.info("Wrong credentials" + e);
            throw new SecurityException("Wrong credentials");

        }
    }

    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
