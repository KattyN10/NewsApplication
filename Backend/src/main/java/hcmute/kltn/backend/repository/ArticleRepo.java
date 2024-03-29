package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepo extends JpaRepository<Article, String> {
}
