package fr.eseo.tauri.validator.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProjectValidator {

	@Min(value = 1, message = "The nbTeams field must be greater than 0")
	@JsonProperty
	private Integer nbTeams;

	@Min(value = 0, message = "The womenPerTeam field must be greater than or equal to 0")
	@JsonProperty
	private Integer womenPerTeam;

	@Min(value = 0, message = "The nbTeams field must be greater than 0")
	@JsonProperty
	private Integer nbSprints;

	@JsonProperty
	private ProjectPhase phase;

}
