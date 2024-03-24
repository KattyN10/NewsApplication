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
@Table(name = "follow_category")
public class FollowCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "follow_category_fk_1")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "article_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "follow_category_fk_2")
    )
    private Article article;
}
