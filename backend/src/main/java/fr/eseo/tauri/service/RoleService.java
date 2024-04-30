package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.util.ListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final AuthService authService;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public List<Role> getAllRoles(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readRoles"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findAll();
	}

	public Role getRoleById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role", id));
	}

	public void addRoles(String token, List<Role> roles) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "addRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		int rolesNumber = roleRepository.findAll().size();
		for(Role role : roles) {
			roleRepository.save(role);
			if(roleRepository.findAll().size() == rolesNumber){
				throw new DataAccessException("Error : Could not add role") {};
			} else {
				rolesNumber++;
			}
		}
	}

	public void updateRole(String token, Integer id, Map<String, Object> roleDetails) {
		if (Boolean.TRUE.equals(authService.checkAuth(token, "updateRole"))) {
			Role role = getRoleById(token, id);

			for (Map.Entry<String, Object> entry : roleDetails.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (value == null) {
					continue;
				}

				switch (key) {
					case "type":
						role.type(RoleType.valueOf((String) value));
						break;
					case "user":
						Map<String, Object> userMap = (Map<String, Object>) value;
						role.user(userRepository.findById((Integer) userMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userMap.get("id"))));
						break;
					default:
						throw new IllegalArgumentException("Invalid key: " + key);
				}
			}

			roleRepository.save(role);

		} else {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
	}

	public void deleteAllRoles(String token) {
		if (Boolean.TRUE.equals(authService.checkAuth(token, "deleteRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		roleRepository.deleteAll();
		if(!roleRepository.findAll().isEmpty()){
			throw new DataAccessException("Error : Could not delete all roles") {};
		}
	}

	public void deleteRole(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		getRoleById(token, id);
		int rolesNumber = roleRepository.findAll().size();
		roleRepository.deleteById(id);
		if(roleRepository.findAll().size() == rolesNumber){
			throw new DataAccessException("Error : Could not delete role with id : " + id) {};
		}
	}

	/**
	 * Fetches all users associated with a specific role type.
	 *
	 * @param roleType The type of role to fetch users for.
	 * @return An iterable of users associated with the provided role type.
	 */
	public Iterable<User> getUsersByRoleType(RoleType roleType) {
		var roles = roleRepository.findByType(roleType);
		return ListUtil.map(roles, Role::user);
	}

}
