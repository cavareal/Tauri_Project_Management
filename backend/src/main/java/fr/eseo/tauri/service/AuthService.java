package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import net.datafaker.providers.base.Bool;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.factory.annotation.Value;
import fr.eseo.tauri.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    @Value("${app.log.with.ldap}")
    private String prodProperty;


    /*public String getNameFromEmail(String email) {
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
    }*/

    public AuthResponse login(String email, String password) {
        try {
            User user;

            if(prodProperty.equals("true")){       // Auth with LDAP
                Authentication authentication = authenticate(email, password);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new SecurityException("Wrong credentials"));
            } else {                               // Auth without LDAP for dev mode
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new SecurityException("Wrong credentials"));
            }

            String accessToken = jwtTokenUtil.generateAccessToken(user);
            CustomLogger.info("Access token generated for user " + user.id() + " : " + accessToken);
            Integer idProject = projectRepository.findFirstByActualTrue().map(Project::id).orElse(0);
            return new AuthResponse(user.id(), accessToken, idProject);
        } catch (Exception e){
            throw new SecurityException("Wrong credentials" + e.getMessage());
        }
    }

    public Authentication authenticate(String email, String password) {
        String safeEmail = StringEscapeUtils.escapeHtml4(email);
        String safePassword = StringEscapeUtils.escapeHtml4(password);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(safeEmail, safePassword)
        );
    }
}
