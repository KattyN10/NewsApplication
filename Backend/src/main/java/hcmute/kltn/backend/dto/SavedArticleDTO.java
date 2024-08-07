package hcmute.kltn.backend.dto;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class SavedArticleDTO {
    private String id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private User user;
}
