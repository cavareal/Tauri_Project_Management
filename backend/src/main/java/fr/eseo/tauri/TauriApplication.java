package fr.eseo.tauri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import me.paulschwarz.springdotenv.DotenvPropertySource;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	public static void main(String[] args) {
		// Run the application
		SpringApplication app = new SpringApplication(TauriApplication.class);
		ConfigurableEnvironment env = app.run(args).getEnvironment();
		DotenvPropertySource.addToEnvironment(env);
	}
}

