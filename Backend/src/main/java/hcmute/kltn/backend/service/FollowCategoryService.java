package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.FollowCategoryDTO;

public interface FollowCategoryService {
    FollowCategoryDTO createFollow(FollowCategoryDTO followCategoryDTO);
    String removeFollow(String categoryId);
}
