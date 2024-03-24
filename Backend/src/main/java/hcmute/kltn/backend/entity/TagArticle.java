package hcmute.kltn.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag_article")
public class TagArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "article_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "tag_article_fk_1")
    )
    private Article article;

    @ManyToOne
    @JoinColumn(
            name = "tags_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "tags_articles_fk_2")
    )
    private Tag tag;
}
