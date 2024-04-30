package fr.eseo.tauri.validator.sprint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateSprintValidator {

    @JsonProperty
    private LocalDate startDate;

    @JsonProperty
    private LocalDate endDate;

    @JsonProperty
    private Integer projectId;
}