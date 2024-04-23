package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.util.ListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

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
