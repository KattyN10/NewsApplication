package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleRepo extends JpaRepository<Article, String> {
    @Query(value = "SELECT * FROM article a WHERE a.category_id = :catId", nativeQuery = true)
    List<Article> findByCatId(String catId);
    
    boolean existsByTitle(String title);
}
