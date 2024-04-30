package fr.eseo.tauri.validator.validationbonus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateValidationBonusValidator {

    @NotNull(message = "The confirmed field is required")
    @JsonProperty
    private Boolean confirmed;

    @NotNull(message = "The user field is required")
    @JsonProperty
    private Integer userId;

    @NotNull(message = "The bonus field is required")
    @JsonProperty
    private Integer bonusId;
}