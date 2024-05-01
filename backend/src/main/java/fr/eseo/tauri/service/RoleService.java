package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.util.ListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final AuthService authService;
	private final RoleRepository roleRepository;
	private final UserService userService;

	public Role getRoleById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role", id));
	}

	public List<Role> getAllRoles(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readRoles"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findAll();
	}

	public void createRole(String token, Role role) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "addRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		role.user(userService.getUserById(token, role.userId()));
		roleRepository.save(role);
	}

	public void updateRole(String token, Integer id, Role updatedRole) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		Role role = getRoleById(token, id);

		if (updatedRole.type() != null) role.type(updatedRole.type());
		if (updatedRole.userId() != null) role.user(userService.getUserById(token, updatedRole.userId()));

		roleRepository.save(role);
	}

	public void deleteRoleById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		getRoleById(token, id);
		roleRepository.deleteById(id);
	}

	public void deleteAllRoles(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		roleRepository.deleteAll();
	}

	/**
	 * Fetches all users associated with a specific role type.
	 *
	 * @param roleType The type of role to fetch users for.
	 * @return An iterable of users associated with the provided role type.
	 */
	public List<User> getUsersByRoleType(String token, RoleType roleType) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteRole"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		var roles = roleRepository.findByType(roleType);
		return ListUtil.map(roles, Role::user);
	}

}