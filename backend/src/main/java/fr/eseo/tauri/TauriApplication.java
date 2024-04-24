package fr.eseo.tauri;

import fr.eseo.tauri.util.CustomLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Value;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	@Value("${prod}")
	private static boolean prod;
	@Value("${databaseUsername}")
	private static String databaseUsernameProd;
	@Value("${databasePassword}")
	private static String databasePasswordProd;

	public static void main(String[] args) {
//		dbConnection();
		// Run the application
		SpringApplication.run(TauriApplication.class, args);
	}

	
	private static void dbConnection() {
		if (prod) { // If build for production deployment
			System.setProperty("spring.datasource.username", databaseUsernameProd);
			System.setProperty("spring.datasource.password", databasePasswordProd);
		} else {
			try {
				Dotenv dotenv = Dotenv.load();
				String databaseUsername = "DATABASE_USERNAME";
				String databasePassword = "DATABASE_PASSWORD";

				if (dotenv.get(databaseUsername) != null && dotenv.get(databasePassword) != null) {
					System.setProperty("spring.datasource.username", dotenv.get(databaseUsername));
					System.setProperty("spring.datasource.password", dotenv.get(databasePassword));
				} else {
					throw new Exception("No .env file found");
				}

			} catch (Exception e) {
				CustomLogger.logError("No .env file found, using default values", e);
			}
		}
	}
}

