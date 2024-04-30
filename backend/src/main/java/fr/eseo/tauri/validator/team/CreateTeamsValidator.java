package fr.eseo.tauri.validator.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTeamsValidator {

	@NotNull(message = "The nbTeams field is required")
	@Min(value = 1, message = "The nbTeams field must be greater than 0")
	@JsonProperty
	private Integer nbTeams;

	@NotNull(message = "The womenPerTeam field is required")
	@Min(value = 0, message = "The womenPerTeam field must be greater than or equal to 0")
	@JsonProperty
	private Integer womenPerTeam;

}
