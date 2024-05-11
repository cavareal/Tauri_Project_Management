package fr.eseo.tauri.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.extractToken(request);

        if (token != null && jwtTokenUtil.validateAccessToken(token)) {
            UserDetails userDetails = jwtTokenUtil.createUserDetails(token);
            jwtTokenUtil.setAuthenticationContext(userDetails, request);
        }
        filterChain.doFilter(request, response);
    }
}
