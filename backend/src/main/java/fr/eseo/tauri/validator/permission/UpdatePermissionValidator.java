package fr.eseo.tauri.validator.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.PermissionType;
import fr.eseo.tauri.model.enumeration.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePermissionValidator {

    @JsonProperty
    private PermissionType type;

    @JsonProperty
    private RoleType role;
}