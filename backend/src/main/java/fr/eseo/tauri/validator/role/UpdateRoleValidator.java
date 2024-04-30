package fr.eseo.tauri.validator.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateRoleValidator {

    @JsonProperty
    private RoleType type;

    @JsonProperty
    private Integer userId;
}