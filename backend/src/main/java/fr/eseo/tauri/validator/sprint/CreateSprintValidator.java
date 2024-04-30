package fr.eseo.tauri.validator.sprint;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.SprintEndType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateSprintValidator {

    @NotNull(message = "The startDate field is required")
    @JsonProperty
    private LocalDate startDate;

    @NotNull(message = "The endDate field is required")
    @JsonProperty
    private LocalDate endDate;

    @NotNull(message = "The endType field is required")
    @JsonProperty
    private SprintEndType endType;

    @NotNull(message = "The project field is required")
    @JsonProperty
    private Integer projectId;
}