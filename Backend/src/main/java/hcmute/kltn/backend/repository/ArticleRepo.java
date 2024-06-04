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
    @Query(value = """
            SELECT a.* FROM article a JOIN category c ON a.category_id=c.id\s
            WHERE c.parent_id=:categoryId AND a.`status`="PUBLIC" 
            ORDER BY a.create_date DESC
            """, nativeQuery = true)
    List<Article> findByParentCat(String categoryId);

    @Query(value = """
            SELECT a.* FROM article a WHERE a.`status`=\"PUBLIC\" 
            AND a.category_id=:categoryId 
            ORDER BY a.create_date DESC
            """, nativeQuery = true)
    List<Article> findByChildCat(String categoryId);

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
    @Query(value = """
            SELECT a.* FROM article a JOIN category c ON a.category_id=c.id WHERE c.parent_id = :categoryId 
            AND a.status="PUBLIC" AND a.create_date = (SELECT MAX(a.create_date) FROM article a  
            JOIN category c ON a.category_id=c.id WHERE c.parent_id = :categoryId 
            AND a.status="PUBLIC") LIMIT 1
                """, nativeQuery = true)
    Article findLatestArtOfCat(String categoryId);

    // lấy 4 bài viết có SL react max
    @Query(value = """
            SELECT * FROM article a JOIN (SELECT article_id
            FROM react_emotion
            GROUP BY article_id
            ORDER BY COUNT(*) DESC
            LIMIT 6) react ON a.id = react.article_id""",
            nativeQuery = true)
    List<Article> findMostReactArticle();

    @Query("select a from Article a where a.artSource = ?1 and a.status = ?2 order by a.create_date DESC")
    List<Article> findByArtSourceAndStatusOrderByCreate_dateDesc(ArtSource artSource, Status status);

//    @Query(value = "SELECT * FROM article WHERE MATCH(title, abstracts, content) AGAINST(?1) AND `status`=\"PUBLIC\"", nativeQuery = true)
//    List<Article> searchArticle(String keyword);

    @Query(value = """
            SELECT * FROM article WHERE (title LIKE CONCAT('% ', :keyword, ' %') 
            OR abstracts LIKE CONCAT('% ', :keyword, ' %'))AND status='PUBLIC'
            """, nativeQuery = true)
    List<Article> searchArticle(String keyword);

    // editor lấy những bài draft có chuyên mục được editor quản lý
    @Query(value = """  
            SELECT a.* FROM article a JOIN editor_manage_cat e ON a.category_id=e.category_id\s
            WHERE a.`status`="DRAFT" AND e.editor_id=:editorId\s
            ORDER BY a.create_date DESC""", nativeQuery = true)
    List<Article> findDraftArticle(String editorId);

    // lấy article list theo tag
    @Query(value = """
            SELECT a.* FROM `article` a JOIN tag_article t ON a.id=t.article_id\s
            WHERE t.tags_id=:tagId AND a.`status`="PUBLIC"\s
            ORDER BY a.create_date DESC
            """, nativeQuery = true)
    List<Article> findByTag(String tagId);

    // writer lấy list non-public cá nhân
    @Query(value = """
            SELECT a.*, u.firstname FROM article a JOIN user u ON a.writer_id=u.id 
            WHERE (a.status='DRAFT' OR a.status='REFUSED') 
            AND u.id=:writerId 
            ORDER BY a.create_date DESC
                        """, nativeQuery = true)
    List<Article> writerGetNonPublicArt(String writerId);

    // writer lấy list public cá nhân
    @Query(value = """
            SELECT a.*, u.firstname FROM article a JOIN user u ON a.writer_id=u.id
            WHERE a.status='PUBLIC' AND u.id=:writerId
            ORDER BY a.create_date DESC
            """, nativeQuery = true)
    List<Article> writerGetPublicArt(String writerId);

    // lấy list order by average star DESC
    @Query(value = """
            SELECT article.* FROM article JOIN average_star ON article.id = average_star.article_id 
                             ORDER BY average_star.average_star DESC
                        """,nativeQuery = true)
    List<Article> getArticleOrderByAverageStar();


}
