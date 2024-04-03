package fr.eseo.tauri.config;

import fr.eseo.tauri.seeder.StudentSeeder;
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

	private final StudentSeeder studentSeeder;

	@Autowired
	public SeedConfig(UserSeeder userSeeder, StudentSeeder studentSeeder) {
		this.userSeeder = userSeeder;
		this.studentSeeder = studentSeeder;
		this.faker = new Faker(new Locale("en-US"));
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		userSeeder.seed(faker);
		studentSeeder.seed(faker);
	}

}
