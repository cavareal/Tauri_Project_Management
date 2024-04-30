package fr.eseo.tauri.validator.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePermissionValidator {

    @NotNull(message = "The type field is required")
    @JsonProperty
    private PermissionType type;

    @NotNull(message = "The role field is required")
    @JsonProperty
    private RoleType role;

    @NotNull(message = "The user field is required")
    @JsonProperty
    private Integer userId;
}