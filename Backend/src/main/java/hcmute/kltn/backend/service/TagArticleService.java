package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.TagArticleDTO;

import java.util.List;

public interface TagArticleService {
    List<TagArticleDTO> getTagsOfArticle(String articleId);
}
