package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepo extends JpaRepository<SavedArticle, String> {
    // lấy bài viết đã lưu của user
    @Query("SELECT new SavedArticle (s.id, s.article, s.user) " +
            "FROM SavedArticle s WHERE s.user.id = :userId")
    List<SavedArticle> findByUserId(String userId);
}
