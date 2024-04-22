package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.TagArticleDTO;
import hcmute.kltn.backend.service.TagArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/tags-article")
@RequiredArgsConstructor
public class TagArticleController {
    private final TagArticleService tagArticleService;

    // lấy list các tags của bài viết
    @GetMapping("/get-tags")
    public ResponseEntity<List<TagArticleDTO>> getTagsOfArt(
            @RequestParam("articleId") String articleId){
        return ResponseEntity.ok(tagArticleService.getTagsOfArticle(articleId));
    }
}
