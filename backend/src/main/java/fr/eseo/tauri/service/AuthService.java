package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Project;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Boolean checkAuth(String token, String permission) {
        // Check if user's token can do this request
        return true;
    }

}
