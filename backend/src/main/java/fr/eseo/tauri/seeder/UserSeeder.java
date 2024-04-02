package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class UserSeeder {

	private final UserRepository userRepository;

	public UserSeeder(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void seed(Faker faker) {
		for (int i = 0; i < 100; i++) {
			var user = new User();
			user.name(faker.name().fullName());
			user.email(faker.internet().emailAddress());
			user.password(faker.internet().password());
			user.privateKey(faker.number().digits(20));
			userRepository.save(user);
		}
	}

}
