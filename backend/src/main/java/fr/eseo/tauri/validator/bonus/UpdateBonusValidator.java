package fr.eseo.tauri.validator.bonus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateBonusValidator {

    @JsonProperty
    private Float value;

    @JsonProperty
    private String comment;

    @JsonProperty
    private Boolean limited;

    @JsonProperty
    private Integer sprintId;

    @JsonProperty
    private Integer studentId;

    @JsonProperty
    private Integer authorId;
}