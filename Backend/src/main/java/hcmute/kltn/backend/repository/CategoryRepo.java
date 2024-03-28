package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
    boolean existsByName(String name);

    @Query("SELECT new hcmute.kltn.backend.entity.Category(c.id, c.name, c.parent) FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findChildCategories(String parentId);

    @Query(value = "SELECT * FROM category c WHERE c.parent_id IS NULL", nativeQuery = true)
    List<Category> findParentCategories();
}
