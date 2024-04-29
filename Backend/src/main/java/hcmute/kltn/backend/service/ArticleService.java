package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    ArticleDTO createArticle(MultipartFile file, ArticleRequest articleRequest);

    String deleteArticle(String id);

    ArticleDTO updateArticle(String id, MultipartFile file, ArticleRequest articleRequest);

    ArticleDTO findById(String id);

    // (Page Home - Top Stories)
    List<ArticleDTO> getTopStarArticle();

    List<ArticleDTO> getTop4NewestArticle();

    // (Page Home - Latest News)
    List<ArticleDTO> getLatestArtPerCat();

//    List<ArticleDTO> findByCatId(String id, int page, int size);

//    List<ArticleDTO> searchArticle(String key, int page, int size);
}
