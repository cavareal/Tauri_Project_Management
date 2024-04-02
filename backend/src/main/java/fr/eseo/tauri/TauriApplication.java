package fr.eseo.tauri;

import fr.eseo.tauri.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.eseo.tauri")
public class TauriApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TauriApplication.class, args);
		StudentService studentService = context.getBean(StudentService.class);
		studentService.populateDatabaseFromCsv("C:\\Users\\coren\\Downloads\\Equipes1.csv");
	}

}
