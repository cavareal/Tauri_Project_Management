package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RoleSeeder {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Autowired
    public RoleSeeder(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void seed() {
        var users = userRepository.findAll();
        Random random = new Random();

        for (var user : users) {
            int randomIndex = random.nextInt(RoleType.values().length);
            RoleType randomRoleType = RoleType.values()[randomIndex];

            var role = new Role();
            role.type(randomRoleType);
            role.user(user);
            roleRepository.save(role);
        }
    }

}
