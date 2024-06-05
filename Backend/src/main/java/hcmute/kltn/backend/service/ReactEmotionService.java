package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.ReactEmotionDTO;
import hcmute.kltn.backend.entity.enum_entity.TypeReact;

import java.util.List;

public interface ReactEmotionService {
    ReactEmotionDTO reactEmotion(ReactEmotionDTO reactEmotionDTO);
    int getReactQuantity(String articleId, TypeReact typeReact);
}
