package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.FeedbackDTO;

import java.util.List;

public interface FeedbackService {
    List<FeedbackDTO> getFeedbackList();
}
