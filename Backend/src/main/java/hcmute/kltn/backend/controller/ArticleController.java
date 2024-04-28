package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.entity.Article;
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
            @RequestPart("file") MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest) {
        return ResponseEntity.ok(articleService.createArticle(file, articleRequest));
    }

    @PostMapping("/update")
    public ResponseEntity<ArticleDTO> updateArticle(
            @RequestParam("articleId") String articleId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest) {
        return ResponseEntity.ok(articleService.updateArticle(articleId, file, articleRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteArticle(
            @RequestParam("articleId") String articleId) {
        return ResponseEntity.ok(articleService.deleteArticle(articleId));
    }

    @GetMapping("/anonymous/get-top-star")
    public ResponseEntity<List<ArticleDTO>> getTopStar(){
        return ResponseEntity.ok(articleService.getTopStarArticle());
    }

    @GetMapping("/anonymous/get-top4-newest")
    public ResponseEntity<List<ArticleDTO>> getTop4Newest(){
        return ResponseEntity.ok(articleService.getTop4NewestArticle());
    }

}
