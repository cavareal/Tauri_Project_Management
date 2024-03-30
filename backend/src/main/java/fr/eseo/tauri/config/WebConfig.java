package fr.eseo.tauri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Spécifiez les URL autorisées pour CORS
                .allowedOrigins("*") // Autoriser les requêtes de toutes les origines
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Autoriser les méthodes HTTP
                .allowedHeaders("*"); // Autoriser tous les en-têtes
    }
}
