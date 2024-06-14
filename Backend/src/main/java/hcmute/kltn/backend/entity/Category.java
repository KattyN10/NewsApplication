package hcmute.kltn.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String second_name;

    @Column(nullable = false)
    private LocalDateTime create_date;

    @ManyToOne
    @JoinColumn(
            name = "parent_id",
            nullable = true,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "category_fk_1")
    )
    private Category parent;
}
