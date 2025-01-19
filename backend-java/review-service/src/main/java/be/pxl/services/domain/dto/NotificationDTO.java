package be.pxl.services.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationDTO that = (NotificationDTO) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(message, that.message) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(approved, that.approved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, message, createdAt, approved);
    }
}

