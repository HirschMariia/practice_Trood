package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequests {
    private String id;
    private String authorId;
    private String recipientId;
    private String projectId;
    private int rating;
    private String text;
    private boolean isPublic;
}
