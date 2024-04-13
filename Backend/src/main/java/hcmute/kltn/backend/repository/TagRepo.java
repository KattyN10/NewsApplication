package hcmute.kltn.backend.repository;

import hcmute.kltn.backend.dto.TagDTO;
import hcmute.kltn.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, String> {
    boolean existsByValue(String value);

    Tag findByValue(String value);
}
