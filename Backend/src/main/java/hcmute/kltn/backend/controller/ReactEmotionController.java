package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ReactEmotionDTO;
import hcmute.kltn.backend.service.ReactEmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/react-emotion")
@RequiredArgsConstructor
public class ReactEmotionController {
    private final ReactEmotionService reactEmotionService;

    @PostMapping("/react")
    public ResponseEntity<ReactEmotionDTO> react(
            @RequestBody ReactEmotionDTO reactEmotionDTO) {
        return ResponseEntity.ok(reactEmotionService.reactEmotion(reactEmotionDTO));
    }

    @GetMapping("/anonymous/get-users-react")
    public ResponseEntity<List<ReactEmotionDTO>> getUsers(
            @RequestBody ReactEmotionDTO reactEmotionDTO){
        return ResponseEntity.ok(reactEmotionService.getUsersReact(reactEmotionDTO));
    }
}
