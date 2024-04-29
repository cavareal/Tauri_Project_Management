package fr.eseo.tauri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {

    @JsonProperty
    private Integer nbStudents;

    @JsonProperty
    private Integer nbWomans;

    @JsonProperty
    private Integer nbBachelors;

    @JsonProperty
    private boolean validCriteriaWoman;

    @JsonProperty
    private boolean validCriteriaBachelor;

    public Criteria(Integer nbWoman, Integer nbBachelor, Integer nbStudents, Boolean validateWoman, Boolean validateBachelor) {
        this.nbWomans = nbWoman;
        this.nbBachelors = nbBachelor;
        this.nbStudents = nbStudents;
        this.validCriteriaWoman = validateWoman;
        this.validCriteriaBachelor = validateBachelor;
    }

}