package be.pxl.services.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class NotificationDTO {

    @JsonProperty("postId")
    private Long postId;

    @JsonProperty("approved")
    private Boolean approved;

    @JsonProperty("message")
    private String message;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonCreator
    public NotificationDTO(
            @JsonProperty("postId") Long postId,
            @JsonProperty("approved") Boolean approved,
            @JsonProperty("message") String message,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.postId = postId;
        this.approved = approved;
        this.message = message;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}

