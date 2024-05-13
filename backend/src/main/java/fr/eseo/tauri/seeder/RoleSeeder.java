package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleSeeder {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public void seed(Faker faker) {
		var users = userRepository.findAll();
		var roleTypes = RoleType.values();

		// Create a project leader role for the admin user
		var rolePl = new Role();
		var userPL = userRepository.findByEmail("pl@tauri.com");
		if (userPL.isPresent()) {
			rolePl.user(userPL.get());
			rolePl.type(RoleType.PROJECT_LEADER);
			roleRepository.save(rolePl);
		}

		// Create a supervising staff role for the supervising staff user
		var roleSS = new Role();
		var userSS = userRepository.findByEmail("ss@tauri.com");
		if (userSS.isPresent()) {
			roleSS.user(userSS.get());
			roleSS.type(RoleType.SUPERVISING_STAFF);
			roleRepository.save(roleSS);
		}

		// Create an option leader role for the option leader user
		var roleOL = new Role();
		var userOL = userRepository.findByEmail("ol@tauri.com");
		if (userOL.isPresent()) {
			roleOL.user(userOL.get());
			roleOL.type(RoleType.OPTION_LEADER);
			roleRepository.save(roleOL);
		}


		// Create one role record for each role type
		for (int i = 1; i < roleTypes.length; i++) {
			var role = new Role();

			role.user(users.get(i));
			role.type(roleTypes[i]);

			roleRepository.save(role);
		}

		// Assign the supervising staff role to several users
		for (var user : users) {
			var userRole = roleRepository.findFirstByUserAndType(user, RoleType.SUPERVISING_STAFF);

			if (faker.number().numberBetween(0, 8) == 0 && userRole == null) {
				var role = new Role();

				role.user(user);
				role.type(RoleType.SUPERVISING_STAFF);

				roleRepository.save(role);
			}
		}
	}

}
