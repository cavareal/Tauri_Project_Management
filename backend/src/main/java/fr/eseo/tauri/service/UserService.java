package fr.eseo.tauri.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.PermissionRepository;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    /**
     * Constructor for UserService.
     * @param teamRepository the team repository
     * @param userRepository the user repository
     * @param roleRepository the role repository
     * @param permissionRepository the permission repository
     */
    @Autowired
    public UserService(UserRepository userRepository, TeamRepository teamRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public int createUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser.id(); //TODO replace by getId() when it will work again
    }

    /**
     * Change team's leader to null when their leader is deleted.
     * @param id the user's id
     */
    public void deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            List<Team> teams = teamRepository.findByLeader(user.get());
            for (Team team : teams) {
                team.leader(null);
                teamRepository.save(team);
            }
            userRepository.deleteById(id);
        }
    }

    public List<PermissionType> getPermissions(Integer id) {
        Optional<User> user = userRepository.findById(id);
        List<PermissionType> permissions = new ArrayList<>();
        if (user.isPresent()) {
            List<Role> roles = roleRepository.findByUser(user.get());
            for(Role role : roles) {
                List<Permission> permissionRelations = permissionRepository.findByRole(role.type());
                for(Permission permission : permissionRelations) {
                    permissions.add(permission.type());
                }
            }
        }
        return permissions;
    }

    public Boolean hasPermission(Integer id, PermissionType permissionRequired) {
        Optional<User> user = userRepository.findById(id);
        List <PermissionType> permissions = this.getPermissions(id);
        if (user.isPresent()) {
            for(PermissionType permission : permissions) {
                if(permission == permissionRequired) {
                    return true;
                }
            }
        }
        return false;
    }

}
