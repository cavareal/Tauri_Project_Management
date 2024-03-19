package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
