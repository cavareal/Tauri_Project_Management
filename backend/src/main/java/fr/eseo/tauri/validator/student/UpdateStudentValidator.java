package fr.eseo.tauri.validator.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStudentValidator {

    @JsonProperty
    private Gender gender;

    @JsonProperty
    private Boolean bachelor;

    @JsonProperty
    private String teamRole;

    @JsonProperty
    private Integer teamId;

}