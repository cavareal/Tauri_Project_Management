package fr.eseo.tauri.validator.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.eseo.tauri.model.enumeration.NotificationType;
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
    private NotificationType type;

    @NotNull(message = "The userToId field is required")
    @JsonProperty
    private Integer userToId;

    @NotNull(message = "The userFromId field is required")
    @JsonProperty
    private Integer userFromId;

}