package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Permission;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
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
	private final PermissionService permissionService;
	private final UserRepository userRepository;

	private static final String READ_PERMISSION = "readRole";
	private static final String ADD_PERMISSION = "addRole";
	private static final String UPDATE_PERMISSION = "updateRole";
	private static final String DELETE_PERMISSION = "deleteRole";

	public Role getRoleById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role", id));
	}

	public List<Role> getAllRoles(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, READ_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		return roleRepository.findAll();
	}

	public void createRole(String token, Role role) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, ADD_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		if(role.userId() != null) role.user(userService.getUserById(token, role.userId()));
		roleRepository.save(role);
	}

	public void createRoles(String email, RoleType[] roles) {
		User user = userRepository.findByEmail(email).orElse(null);

		for (RoleType roleType : roles) {
			Role role = new Role();
			role.user(user);
			role.type(roleType);

			roleRepository.save(role);
		}
	}

	public void updateRole(String token, Integer id, Role updatedRole) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, UPDATE_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		Role role = getRoleById(token, id);

		if (updatedRole.type() != null) role.type(updatedRole.type());
		if (updatedRole.userId() != null) role.user(userService.getUserById(token, updatedRole.userId()));

		roleRepository.save(role);
	}

	public void deleteRoleById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		getRoleById(token, id);
		roleRepository.deleteById(id);
	}

	public void deleteAllRoles(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
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
		if (!Boolean.TRUE.equals(authService.checkAuth(token, DELETE_PERMISSION))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		var roles = roleRepository.findByType(roleType);
		return ListUtil.map(roles, Role::user);
	}

	public Boolean hasPermission(String token, RoleType roleType, PermissionType permissionType) {
		var permissions = permissionService.getAllPermissionsByRole(token, roleType);
		return ListUtil.map(permissions, Permission::type).contains(permissionType);
	}



}