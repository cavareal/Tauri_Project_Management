package fr.eseo.tauri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	public static void main(String[] args) {
		// Run the application
		SpringApplication.run(TauriApplication.class, args);
		String username = System.getenv("DATABASE_USERNAME");
		String password = System.getenv("DATABASE_PASSWORD");
		System.out.println("Database Username: " + username);
		System.out.println("Database Password: " + password);
	}
}

