package fr.eseo.tauri.validator.flag;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.FlagType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateFlagValidator {

    @NotNull(message = "The description field is required")
    @JsonProperty
    private String description;

    @NotNull(message = "The type field is required")
    @JsonProperty
    private FlagType type;

    @NotNull(message = "The firstStudent field is required")
    @JsonProperty
    private Integer firstStudentId;

    @NotNull(message = "The secondStudent field is required")
    @JsonProperty
    private Integer secondStudentId;

    @NotNull(message = "The author field is required")
    @JsonProperty
    private Integer authorId;
}