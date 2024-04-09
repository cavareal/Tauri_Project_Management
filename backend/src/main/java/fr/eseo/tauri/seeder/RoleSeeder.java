package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        RoleType[] roles = {RoleType.SUPERVISING_STAFF, RoleType.TECHNICAL_COACH};

        for (var user : users) {
            if (user.id() < 41) {
                int randomIndex = random.nextInt(roles.length);
                RoleType randomRoleType = roles[randomIndex];

                var role = new Role();
                role.type(randomRoleType);
                role.user(user);
                roleRepository.save(role);
            }
        }
    }

}
