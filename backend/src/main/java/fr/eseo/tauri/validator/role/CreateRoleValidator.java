package fr.eseo.tauri.validator.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateRoleValidator {

    @NotNull(message = "The type field is required")
    @JsonProperty
    private RoleType type;

    @NotNull(message = "The user field is required")
    @JsonProperty
    private Integer userId;
}