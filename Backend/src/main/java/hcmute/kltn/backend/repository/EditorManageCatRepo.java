package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.EditorManageCat;
import hcmute.kltn.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditorManageCatRepo extends JpaRepository<EditorManageCat, String> {
    Boolean existsByCategoryAndUser(Category category, User user);
}
