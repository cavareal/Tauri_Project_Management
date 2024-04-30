package fr.eseo.tauri.validator.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateNotificationValidator {

    @NotNull(message = "The message field is required")
    @JsonProperty
    private String message;

    @NotNull(message = "The isRead field is required")
    @JsonProperty
    private Boolean isRead;

    @NotNull(message = "The type field is required")
    @JsonProperty
    private String type;

    @NotNull(message = "The userTo field is required")
    @JsonProperty
    private Integer userToId;

    @NotNull(message = "The userFrom field is required")
    @JsonProperty
    private Integer userFromId;
}