package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.security.ApplicationSecurity;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSeeder {

	private static final int NB_USERS = 37;

	private final UserRepository userRepository;
	private final ApplicationSecurity applicationSecurity;


	@Value("${app.pl.email}")
	private String plEmail;
	@Value("${app.pl.name}")
	private String plName;

	public void seed(Faker faker) {


		var userPL = new User(plEmail);
		userPL.name(plName);
		userRepository.save(userPL);

//		var userSS = new User("s.s@tauri.com");
//		userSS.name("CLAVREUL Michael");
//		userRepository.save(userSS);
//
//		var userOL = new User("o.l@tauri.com");
//		userOL.name("ROUSSEAU Sophie");
//		userRepository.save(userOL);
//
//		var userTC = new User("t.c@tauri.com");
//		userTC.name("Technique Coach");
//		userRepository.save(userTC);


//		for (int i = 0; i < NB_USERS; i++) {
//			var user = new User();
//
//			user.name(faker.name().lastName().toUpperCase() + " " + faker.name().firstName());
//			user.email(faker.internet().emailAddress());
//			user.password(applicationSecurity.passwordEncoder().encode("password"));
//			user.privateKey(faker.number().digits(20));
//
//			userRepository.save(user);
//		}
	}
}
