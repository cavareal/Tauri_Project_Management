package fr.eseo.tauri.config;

import fr.eseo.tauri.seeder.*;
import fr.eseo.tauri.util.ListUtil;
import net.datafaker.Faker;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SeedConfig implements ApplicationListener<ContextRefreshedEvent> {

	private static final String FAKER_LANGUAGE = "fr-FR";
	private static final String[] HIBERNATE_MODES = {"create", "create-drop"};

	private final Faker faker;

	private final UserSeeder userSeeder;
	private final GradeTypeSeeder gradeTypeSeeder;
	private final PermissionSeeder permissionSeeder;
	private final RoleSeeder roleSeeder;
	private final ProjectSeeder projectSeeder;
	private final NotificationSeeder notificationSeeder;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String hibernateMode;

	@Autowired
	public SeedConfig(UserSeeder userSeeder, GradeTypeSeeder gradeTypeSeeder, PermissionSeeder permissionSeeder, RoleSeeder roleSeeder, ProjectSeeder projectSeeder) {
		this.faker = new Faker(Locale.forLanguageTag(FAKER_LANGUAGE));

		this.userSeeder = userSeeder;
		this.gradeTypeSeeder = gradeTypeSeeder;
		this.permissionSeeder = permissionSeeder;
		this.roleSeeder = roleSeeder;
		this.projectSeeder = projectSeeder;
		this.notificationSeeder = notificationSeeder;
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		if (!ListUtil.contains(List.of(HIBERNATE_MODES), hibernateMode)) return;

		userSeeder.seed(faker);
		gradeTypeSeeder.seed();
		permissionSeeder.seed();
		roleSeeder.seed();
		notificationSeeder.seed(faker);
		projectSeeder.seed();
	}

}
