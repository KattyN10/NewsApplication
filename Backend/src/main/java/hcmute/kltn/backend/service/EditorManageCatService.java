package hcmute.kltn.backend.service;
import hcmute.kltn.backend.dto.EditorManageCatDTO;

import java.util.List;

public interface EditorManageCatService {
    EditorManageCatDTO createManagement(EditorManageCatDTO editorManageCatDTO);
    EditorManageCatDTO updateManagement(String id, EditorManageCatDTO editorManageCatDTO);
    String deleteManagement(String id);
    List<EditorManageCatDTO> findAllManagement();
}
