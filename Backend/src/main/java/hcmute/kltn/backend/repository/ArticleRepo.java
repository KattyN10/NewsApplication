package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.enum_entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleRepo extends JpaRepository<Article, String> {
    @Query(value = "SELECT * FROM article a WHERE a.category_id = :catId AND a.`status`=\"PUBLIC\" " +
            "ORDER BY a.create_date DESC", nativeQuery = true)
    List<Article> findByCatId(String catId);

    boolean existsByTitle(String title);

    @Query("select a from Article a where a.category = ?1 and a.status = ?2")
    List<Article> findByCategoryAndStatus(Category category, Status status);



}
