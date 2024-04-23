package fr.eseo.tauri;

import fr.eseo.tauri.util.CustomLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	public static void main(String[] args) {
			try {
				Dotenv dotenv = Dotenv.load();
				if(dotenv.get("DATABASE_USERNAME") != null && dotenv.get("DATABASE_PASSWORD") != null) {
					System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USERNAME"));
					System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));
				}
			} catch (Exception e) {
				CustomLogger.logError("No .env file found, using default values", e);
			}

		// Run the application
		SpringApplication.run(TauriApplication.class, args);
	}

}

