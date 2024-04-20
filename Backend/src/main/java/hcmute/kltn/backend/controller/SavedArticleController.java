package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.SavedArticleDTO;
import hcmute.kltn.backend.service.SavedArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/saved-articles")
@RequiredArgsConstructor
public class SavedArticleController {
    private final SavedArticleService savedArticleService;

    // lưu bài viết vào danh sách
    @PostMapping("/add")
    public ResponseEntity<SavedArticleDTO> addToList(@RequestBody SavedArticleDTO savedArticleDTO) {
        return ResponseEntity.ok(savedArticleService.addToList(savedArticleDTO));
    }

    // xóa bài viết khỏi danh sách
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromList(
            @RequestParam("id") String id) {
        return ResponseEntity.ok(savedArticleService.removeFromList(id));
    }

    // danh sách các bài viết đã lưu
    @GetMapping("/get-list")
    public ResponseEntity<List<SavedArticleDTO>> getList(
            @RequestParam("userId") String userId){
        return ResponseEntity.ok(savedArticleService.findList(userId));
    }

    // lấy thông tin một bài viết trong danh sách đã lưu
    @GetMapping("get-one")
    public ResponseEntity<SavedArticleDTO> getOne(
            @RequestParam("id") String id){
        return ResponseEntity.ok(savedArticleService.findById(id));
    }
}
