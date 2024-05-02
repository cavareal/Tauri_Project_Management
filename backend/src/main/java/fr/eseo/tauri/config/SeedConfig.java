package fr.eseo.tauri.config;

import fr.eseo.tauri.seeder.*;
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
	private final GradeTypeSeeder gradeTypeSeeder;
	private final PermissionSeeder permissionSeeder;
	private final RoleSeeder roleSeeder;

	private final NotificationSeeder notificationSeeder;

	@Autowired
	public SeedConfig(
            UserSeeder userSeeder, GradeTypeSeeder gradeTypeSeeder, PermissionSeeder permissionSeeder, RoleSeeder roleSeeder, NotificationSeeder notificationSeeder
    ) {

        this.faker = new Faker(new Locale("fr-FR"));

		this.userSeeder = userSeeder;
		this.gradeTypeSeeder = gradeTypeSeeder;
		this.permissionSeeder = permissionSeeder;
		this.roleSeeder = roleSeeder;
		this.notificationSeeder = notificationSeeder;
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		userSeeder.seed(faker);
		gradeTypeSeeder.seed();
		permissionSeeder.seed();
		roleSeeder.seed(faker);
		notificationSeeder.seed(faker);
	}

}
