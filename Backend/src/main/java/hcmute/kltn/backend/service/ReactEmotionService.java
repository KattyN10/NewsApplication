package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.ReactEmotionDTO;

import java.util.List;

public interface ReactEmotionService {
    ReactEmotionDTO reactEmotion(ReactEmotionDTO reactEmotionDTO);
    List<ReactEmotionDTO> getUsersReact(ReactEmotionDTO reactEmotionDTO);
}
