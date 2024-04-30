package fr.eseo.tauri.validator.gradetype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGradeTypeValidator {

    @JsonProperty
    private String name;

    @JsonProperty
    private Float factor;

    @JsonProperty
    private Boolean forGroup;

    @JsonProperty
    private Boolean imported;

    @JsonProperty
    private String scaleUrl;
}