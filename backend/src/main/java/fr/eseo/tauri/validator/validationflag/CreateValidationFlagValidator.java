package fr.eseo.tauri.validator.validationflag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateValidationFlagValidator {

    @NotNull(message = "The confirmed field is required")
    @JsonProperty
    private Boolean confirmed;

    @NotNull(message = "The author field is required")
    @JsonProperty
    private Integer authorId;

    @NotNull(message = "The flag field is required")
    @JsonProperty
    private Integer flagId;
}