package fr.eseo.tauri.repository;

import fr.eseo.tauri.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
