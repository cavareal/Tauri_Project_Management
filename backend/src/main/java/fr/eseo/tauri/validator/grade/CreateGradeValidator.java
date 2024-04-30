package fr.eseo.tauri.validator.grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateGradeValidator {

    @NotNull(message = "The value field is required")
    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @NotNull(message = "The gradeType field is required")
    @JsonProperty
    private Integer gradeTypeId;

    @NotNull(message = "The author field is required")
    @JsonProperty
    private Integer authorId;

    @JsonProperty
    private Integer studentId;

    @JsonProperty
    private Integer teamId;

    @JsonProperty
    private Integer sprintId;
}