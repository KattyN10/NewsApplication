package hcmute.kltn.backend.entity;

import hcmute.kltn.backend.entity.enum_entity.TypeReact;
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
@Table(name = "react_emotion")
public class ReactEmotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "article_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "react_emotion_fk_1")
    )
    private Article article;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "react_emotion_fk_2")
    )
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeReact typeReact;
}
