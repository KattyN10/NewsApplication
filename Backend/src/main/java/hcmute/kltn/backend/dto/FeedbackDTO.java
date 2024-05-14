package hcmute.kltn.backend.dto;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class FeedbackDTO {
    private String id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Article article;
    private String feedback;

}
