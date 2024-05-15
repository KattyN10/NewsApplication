package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.FeedbackDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.dto.request.SearchRequest;
import hcmute.kltn.backend.dto.request.TagArticleRequest;
import hcmute.kltn.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<ArticleDTO> createArticle(
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest,
            @RequestPart(value = "tag", required = false) TagArticleRequest tagArticleRequest) {
        return ResponseEntity.ok(articleService.createArticle(file, articleRequest, tagArticleRequest));
    }

    @PostMapping("/update")
    public ResponseEntity<ArticleDTO> updateArticle(
            @RequestPart("articleId") String articleId,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest,
            @RequestPart(value = "tag", required = false) TagArticleRequest tagArticleRequest) {
        return ResponseEntity.ok(articleService.updateArticle(articleId, file, articleRequest, tagArticleRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteArticle(
            @RequestParam("articleId") String articleId) {
        return ResponseEntity.ok(articleService.deleteArticle(articleId));
    }

    @GetMapping("/anonymous/get-top3-star")
    public ResponseEntity<List<ArticleDTO>> getTop3Star() {
        return ResponseEntity.ok(articleService.getTop3StarArticle());
    }

    @GetMapping("/anonymous/get-top5-newest")
    public ResponseEntity<List<ArticleDTO>> getTop5Newest() {
        return ResponseEntity.ok(articleService.getTop5NewestArticle());
    }

    @GetMapping("/anonymous/get-latest-per-parent-cat")
    public ResponseEntity<List<ArticleDTO>> getLatestPer4Cat() {
        return ResponseEntity.ok(articleService.getLatestArtPer4Cat());
    }

    @GetMapping("/anonymous/get-top6-react-article")
    public ResponseEntity<List<ArticleDTO>> getTop6ReactArticle() {
        return ResponseEntity.ok(articleService.getTop6ReactArt());
    }

    @GetMapping("/anonymous/get-latest-vnexpress")
    public ResponseEntity<List<ArticleDTO>> getLatestVnExpress() {
        return ResponseEntity.ok(articleService.getLatestByVnExpress());
    }

    @GetMapping("/anonymous/get-latest-dantri")
    public ResponseEntity<List<ArticleDTO>> getLatestDanTri() {
        return ResponseEntity.ok(articleService.getLatestByDanTri());
    }

    @GetMapping("/anonymous/get-random-same-category")
    public ResponseEntity<List<ArticleDTO>> getRandomArtSameCat(
            @RequestParam("categoryId") String categoryId) {
        return ResponseEntity.ok(articleService.getRandomArtSameCat(categoryId));
    }

    @GetMapping("/anonymous/search")
    public ResponseEntity<List<ArticleDTO>> searchArticle(
            @RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(articleService.searchArticle(searchRequest));
    }

    @GetMapping("/anonymous/find-by-category")
    public ResponseEntity<List<ArticleDTO>> findByCat(
            @RequestParam("categoryId") String categoryId) {
        return ResponseEntity.ok(articleService.findByCatId(categoryId));
    }

    @GetMapping("/find-draft")
    public ResponseEntity<List<ArticleDTO>> findDraft() {
        return ResponseEntity.ok(articleService.findDraftArticles());
    }

    @PostMapping("/public-article")
    public ResponseEntity<ArticleDTO> publicArt(@RequestParam("articleId") String id) {
        return ResponseEntity.ok(articleService.publicArticle(id));
    }

    @PostMapping("/refuse-article")
    public ResponseEntity<ArticleDTO> refuseArt(@RequestBody FeedbackDTO feedbackDTO) {
        return ResponseEntity.ok(articleService.refuseArticle(feedbackDTO));
    }

}
