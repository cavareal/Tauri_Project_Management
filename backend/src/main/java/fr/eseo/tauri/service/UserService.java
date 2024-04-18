package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.repository.PermissionRepository;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

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
