package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.*;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.util.ListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final AuthService authService;
	private final RoleRepository roleRepository;
	private final UserService userService;
	private final PermissionService permissionService;
	private final UserRepository userRepository;
	private final StudentRepository studentRepository;
	private final GradeRepository gradeRepository;
	private final GradeTypeRepository gradeTypeRepository;

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

	public void createRoles(String email, RoleType[] roles, Integer projectId) {
		User user = userRepository.findByEmail(email).orElse(null);

		for (RoleType roleType : roles) {
			if(roleType == RoleType.OPTION_STUDENT) {
                userRepository.findByEmail(email).ifPresent(userRepository::delete);
                createStudentRoleAndGrades(email, projectId, roleType);
			} else {
				Role role = new Role();
				role.user(user);
				role.type(roleType);

				roleRepository.save(role);
			}
		}
	}

	private void createStudentRoleAndGrades(String email,Integer projectId, RoleType roleType){

		Student student = new Student();
		student.project(new Project().id(projectId));
		student.email(email);
		student.name(emailToName(email));
		studentRepository.save(student);

		Role role = new Role();
		role.user(student);
		role.type(roleType);
		roleRepository.save(role);

		GradeType gradeType = gradeTypeRepository.findByNameAndProjectId("MOYENNE", projectId);

		if (gradeType != null) {
			Grade grade = new Grade();
			grade.value(10f);
			grade.gradeType(gradeType);
			grade.student(student);
			grade.confirmed(false);
			gradeRepository.save(grade);
		}
	}

	private String emailToName(String email){
		String[] parts = email.split("@")[0].split("\\.");
		return (parts.length == 2)
				? parts[1].toUpperCase() + " " + parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase()
				: "";
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