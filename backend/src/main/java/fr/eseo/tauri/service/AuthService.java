package fr.eseo.tauri.service;

import fr.eseo.tauri.util.CustomLogger;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Boolean checkAuth(String token, String permission) {
        CustomLogger.info("Checking if user's token can do this request" + token + " " + permission);
        // Check if user's token can do this request
        //Use the hasPermission method
        // We need to set the

        return true;
    }

}
