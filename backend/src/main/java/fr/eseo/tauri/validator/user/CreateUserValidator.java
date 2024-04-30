package fr.eseo.tauri.validator.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserValidator {

    @NotNull(message = "The name field is required")
    @JsonProperty
    private String name;

    @NotNull(message = "The email field is required")
    @JsonProperty
    private String email;

    @NotNull(message = "The password field is required")
    @JsonProperty
    private String password;

    @JsonProperty
    private String privateKey;
}