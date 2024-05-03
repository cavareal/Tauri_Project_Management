package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SprintEndType {

	NORMAL_SPRINT("Sprint Normal"),
	FINAL_SPRINT("Sprint Final");

	private final String endType;

}
