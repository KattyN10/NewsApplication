package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.FeedbackDTO;
import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/find-feedback")
    public ResponseEntity<List<FeedbackDTO>> findFeedback(){
        return ResponseEntity.ok(feedbackService.getFeedbackList());
    }
}
