package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.FollowCategory;
import hcmute.kltn.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowCategoryRepo extends JpaRepository<FollowCategory, String> {
//    Optional<FollowCategory> findByUserAndCategory(User user, Category category);
    FollowCategory findByUserAndCategory(User user, Category category);

}
