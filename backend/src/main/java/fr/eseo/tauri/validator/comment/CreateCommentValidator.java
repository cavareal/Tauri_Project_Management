package fr.eseo.tauri.validator.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentValidator {

    @NotNull(message = "The content field is required")
    @JsonProperty
    private String content;

    @NotNull(message = "The feedback field is required")
    @JsonProperty
    private Boolean feedback;

    @NotNull(message = "The team field is required")
    @JsonProperty
    private Integer teamId;

    @NotNull(message = "The sprint field is required")
    @JsonProperty
    private Integer sprintId;

    @NotNull(message = "The author field is required")
    @JsonProperty
    private Integer authorId;

}
