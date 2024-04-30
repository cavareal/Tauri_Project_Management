package fr.eseo.tauri.validator.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateCommentValidator {

    @JsonProperty
    private String content;

    @JsonProperty
    private Boolean feedback;

    @JsonProperty
    private Integer teamId;

    @JsonProperty
    private Integer sprintId;

    @JsonProperty
    private Integer authorId;
}