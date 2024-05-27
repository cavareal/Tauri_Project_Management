package fr.eseo.tauri;

import fr.eseo.tauri.util.CustomLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	public static void main(String[] args) {
		// Run the application
		CustomLogger.info("Database username : " + System.getProperty("databaseUsername"));
		SpringApplication.run(TauriApplication.class, args);
	}
}

