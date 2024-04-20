package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.VoteStarDTO;

public interface VoteStarService {
    VoteStarDTO CUDVote(VoteStarDTO voteStarDTO);
    Float getAverageStar(Long articleId);
}
