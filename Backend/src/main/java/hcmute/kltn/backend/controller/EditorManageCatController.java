package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.EditorManageCatDTO;
import hcmute.kltn.backend.service.EditorManageCatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/editor-manage")
@RequiredArgsConstructor
public class EditorManageCatController {
    private final EditorManageCatService editorManageService;

    @PostMapping("/create")
    public ResponseEntity<EditorManageCatDTO> create(
            @RequestBody EditorManageCatDTO editorManageCatDTO){
        return ResponseEntity.ok(editorManageService.createManagement(editorManageCatDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<EditorManageCatDTO> update(
            @RequestParam("id") String id,
            @RequestBody EditorManageCatDTO editorManageCatDTO){
        return ResponseEntity.ok(editorManageService.updateManagement(id, editorManageCatDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteManagement(
            @RequestParam("id") String id){
        return ResponseEntity.ok(editorManageService.deleteManagement(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<EditorManageCatDTO>> getAll(){
        return ResponseEntity.ok(editorManageService.findAllManagement());
    }
}
