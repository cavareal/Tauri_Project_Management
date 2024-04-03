package fr.eseo.tauri.config;

import fr.eseo.tauri.seeder.StudentsWithAverageGradeSeeder;
import fr.eseo.tauri.seeder.UserSeeder;
import net.datafaker.Faker;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SeedConfig implements ApplicationListener<ContextRefreshedEvent> {

	private final Faker faker;
	private final UserSeeder userSeeder;
	private final StudentsWithAverageGradeSeeder studentsWithAverageGradeSeeder;

	@Autowired
	public SeedConfig(UserSeeder userSeeder, StudentsWithAverageGradeSeeder studentsWithAverageGradeSeeder) {
		this.userSeeder = userSeeder;
		this.studentsWithAverageGradeSeeder = studentsWithAverageGradeSeeder;
		this.faker = new Faker(new Locale("en-US"));
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		userSeeder.seed(faker);
		studentsWithAverageGradeSeeder.seed(faker);
	}

}
