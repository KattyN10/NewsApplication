package hcmute.kltn.backend.entity;

import hcmute.kltn.backend.entity.enum_entity.TypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeNotification typeNotification;

    @Column(nullable = false)
    private Timestamp create_date;

    @Column(nullable = false)
    private boolean is_seen;

    @OneToOne
    @JoinColumn(
            name = "feedback_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "notification_fk_1")
    )
    private Feedback feedback;

    @OneToOne
    @JoinColumn(
            name = "voteStar_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "notification_fk_2")
    )
    private VoteStar voteStar;

    @OneToOne
    @JoinColumn(
            name = "comment_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "notification_fk_3")
    )
    private Comment comment;

    @OneToOne
    @JoinColumn(
            name = "reactEmotion_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "notification_fk_4")
    )
    private ReactEmotion reactEmotion;

}
