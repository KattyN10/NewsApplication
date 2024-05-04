package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.dto.request.TagArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    Float readingTime(String content);

    ArticleDTO createArticle(MultipartFile file, ArticleRequest articleRequest, TagArticleRequest tagArticleRequest);

    String deleteArticle(String id);

    ArticleDTO updateArticle(String id, MultipartFile file, ArticleRequest articleRequest, TagArticleRequest tagArticleRequest);

    ArticleDTO findById(String id);

    List<ArticleDTO> getTop3StarArticle();

    List<ArticleDTO> getTop6NewestArticle();

    List<ArticleDTO> getLatestArtPerCat();

    List<ArticleDTO> getTop6ReactArt();

    List<ArticleDTO> getLatestByVnExpress();

    List<ArticleDTO> getLatestByDanTri();

    List<ArticleDTO> getRandomArtSameCat(String catId);

//    List<ArticleDTO> findByCatId(String id, int page, int size);

    List<ArticleDTO> searchArticle(String keyword);
}
