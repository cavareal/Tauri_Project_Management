package fr.eseo.tauri.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum GradeTypeName {

    AVERAGE("average"),
    DEFAULT("default"),
    PADL("PADL"),
    PDLO("PDLO"),
    PWND("PWND"),
    IRS("IRS"),
    STAGE_S7("STAGE S7"),
    S5("S5"),
    S6("S6");

    private final String displayName;

    // TODO: Vérifier si cette fonction est utilisée

    public static GradeTypeName fromDisplayName(String displayName) {
        return Arrays.stream(GradeTypeName.values())
                .filter(gradeTypeName -> gradeTypeName.displayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid display name: " + displayName));
    }

}