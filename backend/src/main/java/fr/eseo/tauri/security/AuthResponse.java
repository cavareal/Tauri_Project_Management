package fr.eseo.tauri.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String accessToken;

    @JsonProperty
    private Integer projectId;
}
