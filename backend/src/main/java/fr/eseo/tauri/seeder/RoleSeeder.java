package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleSeeder {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public RoleSeeder(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public void seed(Faker faker) {
		var users = userRepository.findAll();
		var roleTypes = RoleType.values();

		// Create one role record for each role type
		for (int i = 0; i < roleTypes.length; i++) {
			var role = new Role();

			role.user(users.get(i));
			role.type(roleTypes[i]);

			roleRepository.save(role);
		}

		// Assign the supervising staff role to several users
		for (var user : users) {
			if (faker.number().numberBetween(0, 8) == 0 && !roleRepository.existsByUserAndType(user, RoleType.SUPERVISING_STAFF)) {
				var role = new Role();

				role.user(user);
				role.type(RoleType.SUPERVISING_STAFF);

				roleRepository.save(role);
			}
		}
	}

}
