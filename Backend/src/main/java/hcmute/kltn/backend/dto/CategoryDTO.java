package hcmute.kltn.backend.dto;

import hcmute.kltn.backend.entity.Category;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CategoryDTO {
    String id;
    String name;
    @ManyToOne
    Category parent;
}
