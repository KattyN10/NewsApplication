package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.TagDTO;
import hcmute.kltn.backend.dto.response.ApiResponse;
import hcmute.kltn.backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TagDTO>> createTag (@RequestBody TagDTO tagDTO){
        ApiResponse<TagDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(tagService.createTag(tagDTO));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<TagDTO>> updateTag(
            @RequestParam("tagId") String tagId,
            @RequestBody TagDTO tagDTO){
        ApiResponse<TagDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(tagService.updateTag(tagDTO, tagId));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteTag (@RequestParam("tagId") String tagId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(tagService.deleteTag(tagId));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-all-tags")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getAllTags(){
        ApiResponse<List<TagDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(tagService.findAll());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-tag")
    public ResponseEntity<ApiResponse<TagDTO>> getTag(@RequestParam("tagId") String tagId){
        ApiResponse<TagDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(tagService.findTagById(tagId));
        return ResponseEntity.ok(apiResponse);
    }
}
