package fr.eseo.tauri.model.enumeration;

import lombok.Getter;

@Getter
public enum GradeTypeEnum {

    Overall_Performance_1("Performance global sprint 1");

    private final String gradeType;

    GradeTypeEnum(String gradeType) {
        this.gradeType = gradeType;
    }

}
