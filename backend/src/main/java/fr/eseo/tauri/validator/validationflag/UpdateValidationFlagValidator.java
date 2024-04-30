package fr.eseo.tauri.validator.validationflag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateValidationFlagValidator {

    @JsonProperty
    private Boolean confirmed;

    @JsonProperty
    private Integer authorId;

    @JsonProperty
    private Integer flagId;
}