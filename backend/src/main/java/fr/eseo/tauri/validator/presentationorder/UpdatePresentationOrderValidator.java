package fr.eseo.tauri.validator.presentationorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePresentationOrderValidator {

    @JsonProperty
    private Integer order;

    @JsonProperty
    private Integer projectId;

    @JsonProperty
    private Integer sprintId;
}