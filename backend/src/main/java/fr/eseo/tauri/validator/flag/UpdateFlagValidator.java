package fr.eseo.tauri.validator.flag;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.FlagType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateFlagValidator {

    @JsonProperty
    private String description;

    @JsonProperty
    private FlagType type;

    @JsonProperty
    private Integer firstStudentId;

    @JsonProperty
    private Integer secondStudentId;

    @JsonProperty
    private Integer authorId;
}
