package fr.eseo.tauri.validator.bonus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateBonusValidator {

    @NotNull(message = "The value field is required")
    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @NotNull(message = "The limited field is required")
    @JsonProperty
    private Boolean limited;

    @NotNull(message = "The sprint field is required")
    @JsonProperty
    private Integer sprintId;

    @NotNull(message = "The student field is required")
    @JsonProperty
    private Integer studentId;

    @NotNull(message = "The author field is required")
    @JsonProperty
    private Integer authorId;
}
