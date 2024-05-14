package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.security.ApplicationSecurity;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSeeder {

	private static final int NB_USERS = 37;

	private final UserRepository userRepository;
	private final ApplicationSecurity applicationSecurity;

	public void seed(Faker faker) {
		var userPL = new User("WOODWARD Richard", "pl@tauri.com", applicationSecurity.passwordEncoder().encode("pl"), faker.number().digits(20));
		userRepository.save(userPL);

		var userSS = new User("CLAVREUL Mickaël", "ss@tauri.com", applicationSecurity.passwordEncoder().encode("ss"), faker.number().digits(20));
		userRepository.save(userSS);

		var userOL = new User("ROUSSEAU Sophie", "ol@tauri.com", applicationSecurity.passwordEncoder().encode("ol"), faker.number().digits(20));
		userRepository.save(userOL);

		var userTC = new User("LECLAIRE Clément", "tc@tauri.com", applicationSecurity.passwordEncoder().encode("tc"), faker.number().digits(20));
		userRepository.save(userTC);


		for (int i = 0; i < NB_USERS; i++) {
			var user = new User();

			user.name(faker.name().lastName().toUpperCase() + " " + faker.name().firstName());
			user.email(faker.internet().emailAddress());
			user.password(applicationSecurity.passwordEncoder().encode("password"));
			user.privateKey(faker.number().digits(20));

			userRepository.save(user);
		}
	}
}
