package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.enum_entity.ArtSource;
import hcmute.kltn.backend.entity.enum_entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<Article, String> {
    // tìm cat theo catId
    @Query(value = "SELECT * FROM article a WHERE a.category_id = :catId AND a.`status`=\"PUBLIC\" " +
            "ORDER BY a.create_date DESC", nativeQuery = true)
    List<Article> findByCatId(String catId);

    // check exists article by title and abstracts
    boolean existsByTitleOrAbstracts(String title, String abstracts);

    @Query("select (count(a) > 0) from Article a where a.artSource = ?1 and a.avatar = ?2 and a.create_date = ?3")
    boolean existsByArtSourceAndAvatarAndCreate_date(ArtSource artSource, String avatar, LocalDateTime createDate);

    // tìm article by category and status
    @Query("select a from Article a where a.category = ?1 and a.status = ?2")
    List<Article> findByCategoryAndStatus(Category category, Status status);

    // tìm article by status
    List<Article> findByStatus(Status status);

    // tìm article by status order by create_date DESC
    @Query("select a from Article a where a.status = ?1 order by a.create_date DESC")
    List<Article> findByStatusOrderByCreate_dateDesc(Status status);

    // tìm latest article mỗi parent category
    @Query(value = "SELECT a.* FROM article a JOIN category c ON a.category_id=c.id " +
            "WHERE c.parent_id = :categoryId AND a.status=\"PUBLIC\" AND a.create_date = " +
            "(SELECT MAX(a.create_date) FROM article a  JOIN category c ON a.category_id=c.id " +
            "WHERE c.parent_id = :categoryId AND a.status=\"PUBLIC\")", nativeQuery = true)
    Article findLatestArtPerCat(String categoryId);

    // lấy 4 bài viết có SL react max
    @Query(value = """
            SELECT * FROM article a JOIN (SELECT article_id
            FROM react_emotion
            GROUP BY article_id
            ORDER BY COUNT(*) DESC
            LIMIT 6) react ON a.id = react.article_id""",
            nativeQuery = true)
    List<Article> findMostReactArticle();

    @Query("select a from Article a where a.artSource = ?1 order by a.create_date DESC")
    List<Article> findByArtSourceOrderByCreate_dateDesc(ArtSource artSource);

    @Query(value = "SELECT * FROM article WHERE MATCH(title, abstracts, content) AGAINST(?1) AND `status`=\"PUBLIC\"", nativeQuery = true)
    List<Article> searchArticle(String keyword);


}
