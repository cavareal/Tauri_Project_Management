package fr.eseo.tauri.validator.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTeamValidator {

    @JsonProperty
    private String name;

    @JsonProperty
    private Integer leaderId;
}