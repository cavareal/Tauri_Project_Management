package fr.eseo.tauri.validator.grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGradeValidator {

    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @JsonProperty
    private Integer gradeTypeId;

    @JsonProperty
    private Integer authorId;

    @JsonProperty
    private Integer studentId;

    @JsonProperty
    private Integer teamId;

    @JsonProperty
    private Integer sprintId;
}