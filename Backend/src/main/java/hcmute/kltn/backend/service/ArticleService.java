package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {
    ArticleDTO createArticle(MultipartFile file, ArticleRequest articleRequest);
    String deleteArticle(String id);
    ArticleDTO updateArticle(String id, MultipartFile file, ArticleRequest articleRequest);
}
