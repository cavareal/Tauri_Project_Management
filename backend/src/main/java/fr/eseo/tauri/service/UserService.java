package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.util.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final AuthService authService;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final RoleRepository roleRepository;
	private final PermissionService permissionService;



	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readUser"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", id));
	}

	public List<User> getAllUsers(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readUser"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		return userRepository.findAll();
	}

	public User createUser(User user) {
		User userCheck = userRepository.findByEmail(user.email()).orElse(null);
		if (userCheck != null) {
			throw new SecurityException("User already exists in the database");
		}
		return userRepository.save(user);
	}

	public void updateUser(String token, Integer id, User updatedUser) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateUser"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		var user = getUserById(token, id);

		if (updatedUser.name() != null) user.name(updatedUser.name());
		if (updatedUser.email() != null) user.email(updatedUser.email());
		if (updatedUser.password() != null) user.password(updatedUser.password());
		if (updatedUser.privateKey() != null) user.privateKey(updatedUser.privateKey());

		userRepository.save(user);
	}

	public void deleteUserById(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteUser"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		var user = getUserById(token, id);

		// Change team's leader to null when their leader is deleted.
		var teams = teamRepository.findAllByLeaderId(user.id());
		for (var team : teams) {
			team.leader(null);
			teamRepository.save(team);
		}

		userRepository.deleteById(id);
	}

	public void deleteAllUsers(String token) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteUser"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		userRepository.deleteAll();
	}

	public List<RoleType> getRolesByUserId(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readRoleByUserId"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		User user = getUserById(token, id);
		return roleRepository.findByUser(user);
	}

	public Team getTeamByMemberId(String token, Integer userId, Integer projectId) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readTeamBySupervisor"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}
		List<RoleType> roles = getRolesByUserId(token, userId);

		if (roles.contains(RoleType.SUPERVISING_STAFF)) {
			return teamRepository.findByLeaderId(userId, projectId);
		} else if (roles.contains(RoleType.TEAM_MEMBER)) {
			return teamRepository.findByStudentId(userId);
		} else {
			CustomLogger.info(getUserById(token, userId).name() + "is not a member of any team");
			return null;
		}
	}

	public List<PermissionType> getPermissionsByUser(String token, Integer id) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermissions"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		var user = getUserById(token, id);
		var roles = roleRepository.findByUser(user);

		List<PermissionType> permissions = new ArrayList<>();

		for (var role : roles) {
			var permissionsRoles = permissionService.getAllPermissionsByRole(token, role);
			for (var permission : permissionsRoles) {
				if (!permissions.contains(permission.type())) permissions.add(permission.type());
			}
		}

		return permissions;
	}

	public Boolean hasPermission(String token, Integer id, PermissionType permission) {
		if (!Boolean.TRUE.equals(authService.checkAuth(token, "readPermissions"))) {
			throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
		}

		var permissions = getPermissionsByUser(token, id);

		return permissions.contains(permission);
	}

}
