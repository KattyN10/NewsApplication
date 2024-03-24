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
@Table(name = "editor_manage_cat")
public class EditorManageCat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "editor_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "editor_manage_cat_fk_1")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "editor_manage_cat_fk_2")
    )
    private Category category;
}
