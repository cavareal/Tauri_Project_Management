package fr.eseo.tauri.validator.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateProjectValidator {

    @JsonProperty
    private Integer nbTeams;

    @JsonProperty
    private Integer womenPerTeam;

    @JsonProperty
    private Integer nbSprints;

    @JsonProperty
    private ProjectPhase phase;
}

