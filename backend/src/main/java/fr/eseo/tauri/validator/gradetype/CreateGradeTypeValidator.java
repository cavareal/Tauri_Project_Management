package fr.eseo.tauri.validator.gradetype;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateGradeTypeValidator {

    @NotNull(message = "The name field is required")
    @JsonProperty
    private String name;

    @NotNull(message = "The factor field is required")
    @JsonProperty
    private Float factor;

    @NotNull(message = "The forGroup field is required")
    @JsonProperty
    private Boolean forGroup;

    @NotNull(message = "The imported field is required")
    @JsonProperty
    private Boolean imported;

    @JsonProperty
    private String scaleUrl;
}