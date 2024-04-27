package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.FollowCategory;
import hcmute.kltn.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowCategoryRepo extends JpaRepository<FollowCategory, String> {
    FollowCategory findByUserAndCategory(User user, Category category);

    List<FollowCategory> findByUser(User user);
}
