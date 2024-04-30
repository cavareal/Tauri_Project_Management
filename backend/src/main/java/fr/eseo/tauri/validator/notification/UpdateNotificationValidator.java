package fr.eseo.tauri.validator.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateNotificationValidator {

    @JsonProperty
    private String message;

    @JsonProperty
    private Boolean isRead;

    @JsonProperty
    private Integer userToId;

    @JsonProperty
    private Integer userFromId;

}