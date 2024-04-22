package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.SavedArticleDTO;

import java.util.List;

public interface SavedArticleService {
    SavedArticleDTO addToList(SavedArticleDTO savedArticleDTO);
    String removeFromList(String id);
    List<SavedArticleDTO> findList();
    SavedArticleDTO findById(String id);
}
