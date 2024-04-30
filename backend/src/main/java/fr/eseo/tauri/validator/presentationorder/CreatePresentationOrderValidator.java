package fr.eseo.tauri.validator.presentationorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePresentationOrderValidator {

    @NotNull(message = "The order field is required")
    @JsonProperty
    private Integer order;

    @NotNull(message = "The team field is required")
    @JsonProperty
    private Integer teamId;

    @NotNull(message = "The project field is required")
    @JsonProperty
    private Integer projectId;
}