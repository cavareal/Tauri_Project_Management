package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Role;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.RoleRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public RoleService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public Iterable<User> getUsersByRoleType(RoleType roleType) {
		var roles = roleRepository.findByType(roleType);
		return ListUtil.map(roles, Role::user);
	}

}
