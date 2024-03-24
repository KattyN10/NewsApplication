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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(/*cascade = CascadeType.REMOVE*/)
    @JoinColumn(
            name = "parent_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "category_fk_1")
    )
    private Category parent;
}
