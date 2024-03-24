package hcmute.kltn.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "article_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "comment_fk_1")
    )
    private Article article;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "comment_fk_2")
    )
    private User user;

    @Column(nullable = false)
    private String comment;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Timestamp create_date;

    @ManyToOne
    @JoinColumn(
            name = "parent_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "comment_fk_3")
    )
    private Comment parent;
}
