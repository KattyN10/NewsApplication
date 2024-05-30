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

    List<ArticleDTO> getTop5NewestArticle();

    List<ArticleDTO> getLatestArtPer4Cat();

    List<ArticleDTO> getLatestArtPerParentCat();

    List<ArticleDTO> getTop6ReactArt();

    List<ArticleDTO> getLatestByVnExpress(int count);

    List<ArticleDTO> getLatestByDanTri(int count);

    List<ArticleDTO> getLatestByPqExpress(int count);

    List<ArticleDTO> getRandomArtSameCat(String catId);

    List<ArticleDTO> findByCatId(String categoryId);

    List<ArticleDTO> findByTagId(String tagId);

    List<ArticleDTO> searchArticle(List<String> keyList);

    List<ArticleDTO> findDraftArticles();

    ArticleDTO publicArticle(String articleId);

    ArticleDTO refuseArticle(String articleId);

    List<ArticleDTO> writerGetListNonPublicArt();

    List<ArticleDTO> writerGetListPublicArt();

//    String uploadCloud();
}
