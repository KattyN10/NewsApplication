package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.VoteStarDTO;
import hcmute.kltn.backend.service.VoteStarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vote-star")
@RequiredArgsConstructor
public class VoteStarController {
    private final VoteStarService voteStarService;

    @PostMapping("/vote")
    public ResponseEntity<VoteStarDTO> vote(
            @RequestBody VoteStarDTO voteArticleDTO){
        return ResponseEntity.ok(voteStarService.CUDVote(voteArticleDTO));
    }

    @GetMapping("/get-average-star")
    public ResponseEntity<Float> getAverageStar(
            @RequestParam("articleId") Long articleId){
        return ResponseEntity.ok(voteStarService.getAverageStar(articleId));
    }
}
