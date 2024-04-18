package fr.eseo.tauri;

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
			System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USERNAME"));
			System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));
		} catch (Exception e){
			System.out.println("No .env file found, using default values");
		}
		// Run the application
		SpringApplication.run(TauriApplication.class, args);
	}

}

