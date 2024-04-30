package fr.eseo.tauri.validator.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateStudentValidator {

    @NotNull(message = "The gender field is required")
    @JsonProperty
    private Gender gender;

    @NotNull(message = "The bachelor field is required")
    @JsonProperty
    private Boolean bachelor;

    @JsonProperty
    private String teamRole;

    @NotNull(message = "The team field is required")
    @JsonProperty
    private Integer teamId;

    @NotNull(message = "The project field is required")
    @JsonProperty
    private Integer projectId;
}