package fr.eseo.tauri.config;

import fr.eseo.tauri.seeder.TeamSeeder;
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

	private final TeamSeeder teamSeeder;

	@Autowired
	public SeedConfig(UserSeeder userSeeder, TeamSeeder teamSeeder) {
		this.faker = new Faker(new Locale("fr-FR"));
		this.userSeeder = userSeeder;
        this.teamSeeder = teamSeeder;
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		userSeeder.seed(faker);
		teamSeeder.seed(faker);
	}

}
