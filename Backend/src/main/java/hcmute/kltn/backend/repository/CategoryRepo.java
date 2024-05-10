package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
    boolean existsByNameAndParent(String name, Category parent);

    @Query("SELECT new hcmute.kltn.backend.entity.Category(c.id, c.name,c.second_name, c.create_date, " +
            "c.parent) FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findChildCategories(String parentId);

    @Query(value = "SELECT * FROM category c WHERE c.parent_id IS NULL", nativeQuery = true)
    List<Category> findParentCategories();

    @Query(value = "SELECT * FROM category c WHERE c.parent_id IS NULL AND c.name = :name ", nativeQuery = true)
    Category findParentCatByName(@Param("name") String name);

    Category findByNameAndParent(String name, Category parent);

    @Query(value = "SELECT * FROM (SELECT * FROM category WHERE category.parent_id IS NOT NULL) c \n" +
            "WHERE c.second_name = :second_name OR c.`name`= :second_name LIMIT 1", nativeQuery = true)
    Category findBySecondOrName(String second_name);

    // lấy 4 chuyên mục cha có số lượng bài viết nhiều nhất
    @Query(value = """
            SELECT result.id, result.name, result.second_name, result.parent_id, result.create_date
            FROM (SELECT parent.*, COUNT(*) AS post_count
            FROM category AS child
            JOIN category AS parent ON child.parent_id = parent.id
            JOIN article ON article.category_id = child.id
            GROUP BY parent.id, parent.name
            ORDER BY post_count DESC
            LIMIT 4) result\s""", nativeQuery = true)
    List<Category> find4ParentCatHaveMaxArticle();

}
