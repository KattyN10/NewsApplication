package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.dto.response.ApiResponse;
import hcmute.kltn.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ArticleDTO>> createArticle(
            @RequestPart("file") MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest) {
        ApiResponse<ArticleDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(articleService.createArticle(file, articleRequest));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<ArticleDTO>> updateArticle(
            @RequestParam("articleId") String articleId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("body") ArticleRequest articleRequest) {
        ApiResponse<ArticleDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(articleService.updateArticle(articleId, file, articleRequest));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteArticle(
            @RequestParam("articleId") String articleId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(articleService.deleteArticle(articleId));
        return ResponseEntity.ok(apiResponse);
    }

}
