package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.FeedbackDTO;
import hcmute.kltn.backend.entity.Feedback;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.FeedbackRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final FeedbackRepo feedbackRepo;

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public List<FeedbackDTO> getFeedbackList() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();
        List<Feedback> feedbackList = feedbackRepo.findFeedbackByWriter(user.getId());
        return feedbackList.stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
                .collect(Collectors.toList());
    }
}
