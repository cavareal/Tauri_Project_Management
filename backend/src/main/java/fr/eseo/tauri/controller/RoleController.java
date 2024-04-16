package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping("/add")
    public Role addRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @GetMapping("/all")
    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Role updateRole(@PathVariable Integer id, @RequestBody Role roleDetails) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.type(roleDetails.type());
            // If you have a User field, you can also update it here
            return roleRepository.save(role);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRole(@PathVariable Integer id) {
        roleRepository.deleteById(id);
        return "Role deleted";
    }

    @GetMapping("/user/{type}")
    public User getFirstUserByRoleType(@PathVariable RoleType type) {
        return roleRepository.findFirstByType(type).user();
    }
}
