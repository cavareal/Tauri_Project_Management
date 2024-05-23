package fr.eseo.tauri.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty
    private String username;

    @JsonProperty
    private String accessToken;
}
