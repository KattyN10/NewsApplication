package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.FollowCategoryDTO;
import hcmute.kltn.backend.service.FollowCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow-category")
@RequiredArgsConstructor
public class FollowCategoryController {
    private final FollowCategoryService followCategoryService;

    @PostMapping("/follow")
    public ResponseEntity<FollowCategoryDTO> followCategory(
            @RequestBody FollowCategoryDTO followCategoryDTO){
        return ResponseEntity.ok(followCategoryService.createFollow(followCategoryDTO));
    }

    @DeleteMapping("/un-follow")
    public ResponseEntity<String> followCategory(
            @RequestParam String categoryId){
        return ResponseEntity.ok(followCategoryService.removeFollow(categoryId));
    }
}
