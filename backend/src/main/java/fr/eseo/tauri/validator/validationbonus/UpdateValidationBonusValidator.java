package fr.eseo.tauri.validator.validationbonus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateValidationBonusValidator {

    @JsonProperty
    private Boolean confirmed;

    @JsonProperty
    private Integer userId;

    @JsonProperty
    private Integer bonusId;
}