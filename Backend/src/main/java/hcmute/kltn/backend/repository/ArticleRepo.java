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
    // tìm cat theo catId
    @Query(value = "SELECT * FROM article a WHERE a.category_id = :catId AND a.`status`=\"PUBLIC\" " +
            "ORDER BY a.create_date DESC", nativeQuery = true)
    List<Article> findByCatId(String catId);

    // check exists article by title and abstracts
    boolean existsByTitleOrAbstracts(String title, String abstracts);

    // tìm article by category and status
    @Query("select a from Article a where a.category = ?1 and a.status = ?2")
    List<Article> findByCategoryAndStatus(Category category, Status status);

    // tìm article by status
    List<Article> findByStatus(Status status);

    // tìm article by status order by create_date DESC
    @Query("select a from Article a where a.status = ?1 order by a.create_date DESC")
    List<Article> findByStatusOrderByCreate_dateDesc(Status status);

    // tìm latest article mỗi parent category
    @Query(value = "SELECT result1.* FROM (SELECT a.*, c.parent_id\n" +
            "FROM article AS a\n" +
            "JOIN category AS c ON a.category_id = c.id\n" +
            "WHERE a.create_date = (\n" +
            "  SELECT MAX(create_date)\n" +
            "  FROM article AS a2\n" +
            "  WHERE a.category_id = a2.category_id\n" +
            ") AND a.`status` = \"PUBLIC\"\n" +
            "GROUP BY c.parent_id) result1 JOIN category c ON result1.parent_id = c.id", nativeQuery = true)
    List<Article> findLatestArtPerParentCat();



}
