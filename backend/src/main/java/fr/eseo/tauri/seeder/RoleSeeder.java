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

		var rolePL = new Role();
		rolePL.user(users.get(0));
		rolePL.type(RoleType.PROJECT_LEADER);
		roleRepository.save(rolePL);

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
