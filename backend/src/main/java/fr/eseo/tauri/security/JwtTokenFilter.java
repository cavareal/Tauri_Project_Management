package fr.eseo.tauri.security;

import fr.eseo.tauri.util.CustomLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.extractToken(request);

        CustomLogger.info(request.getRequestURI());

        if (token != null && jwtTokenUtil.validateAccessToken(token)) {
            UserDetails userDetails = jwtTokenUtil.createUserDetails(token);
            jwtTokenUtil.setAuthenticationContext(userDetails, request);
            filterChain.doFilter(request, response);

        } else if (request.getRequestURI().equals("/api/auth/login")) {
            filterChain.doFilter(request, response);

        } else if (!Objects.equals(request.getRequestURI(), "/api")) {
            filterChain.doFilter(request, response);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
