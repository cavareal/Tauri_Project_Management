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
		// Load database logs
		try{
			Dotenv dotenv = Dotenv.load();
			System.setProperty("spring.datasource.username", System.getenv("DATABASE_USERNAME"));
			System.setProperty("spring.datasource.password", System.getenv("DATABASE_PASSWORD"));
			System.out.println(System.getenv("DATABASE_USERNAME"));
		} catch (Exception e){
			CustomLogger.logError("No .env file found, using default values", e);
		}
		// Run the application
		SpringApplication.run(TauriApplication.class, args);
	}

}

