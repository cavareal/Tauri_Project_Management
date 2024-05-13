package fr.eseo.tauri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


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
		// Run the application
		SpringApplication.run(TauriApplication.class, args);
	}
}

