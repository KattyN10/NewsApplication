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
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "feedback_fk_1")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "article_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "feedback_fk_2")
    )
    private Article article;

    @Column(nullable = false)
    private String feedback;
}
