package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, String> {
    @Query(value = """
            SELECT f.* FROM `feedback` f JOIN article a ON f.article_id = a.id
            WHERE a.writer_id=:writerId
            """, nativeQuery = true)
    List<Feedback> findFeedbackByWriter(String writerId);
}
