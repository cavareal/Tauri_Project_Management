package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSeeder {

	private static final int NB_USERS = 40;

	private final UserRepository userRepository;

	@Autowired
	public UserSeeder(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void seed(Faker faker) {
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
