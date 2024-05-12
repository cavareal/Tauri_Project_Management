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

	private static final int NB_USERS = 39;

	private final UserRepository userRepository;

	private final ApplicationSecurity applicationSecurity;

	public void seed(Faker faker) {
		var userPL = new User();
		userPL.name("Richard Woodward");
		userPL.email("richard.woodward@eseo.fr");
		userPL.password(applicationSecurity.passwordEncoder().encode("projectLeader"));
		userPL.privateKey(faker.number().digits(20));
		userRepository.save(userPL);

		for (int i = 0; i < NB_USERS; i++) {
			var user = new User();

			user.name(faker.name().fullName());
			user.email(faker.internet().emailAddress());
			user.password(faker.internet().password());
			user.privateKey(faker.number().digits(20));

			userRepository.save(user);
		}
	}

}
