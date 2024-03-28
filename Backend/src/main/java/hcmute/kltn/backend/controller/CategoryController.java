package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.CategoryDTO;
import hcmute.kltn.backend.dto.response.ApiResponse;
import hcmute.kltn.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.createCategory(categoryDTO));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @RequestParam("categoryId") String categoryId,
            @RequestBody CategoryDTO categoryDTO) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.updateCategory(categoryDTO, categoryId));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @RequestParam("categoryId") String categoryId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(categoryService.deleteCategory(categoryId));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.findAll());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-category")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(
            @RequestParam("categoryId") String categoryId) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.findCategoryById(categoryId));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-child")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getChildCats(
            @RequestParam("categoryId") String categoryId) {
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.findChildCategories(categoryId));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-parent")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getParentCats() {
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.findParentCategories());
        return ResponseEntity.ok(apiResponse);
    }
}
