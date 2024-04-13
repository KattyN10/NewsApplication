package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.TagArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagArticleRepo extends JpaRepository<TagArticle, String> {

}
