package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.entity.EditorManageCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorManageCatRepo extends JpaRepository<EditorManageCat, String> {
}
