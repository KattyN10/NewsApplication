package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.EditorManageCatDTO;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.EditorManageCat;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.repository.EditorManageCatRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.EditorManageCatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorManageCatServiceImpl implements EditorManageCatService {
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final EditorManageCatRepo editorManageCatRepo;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public EditorManageCatDTO createManagement(EditorManageCatDTO editorManageCatDTO) {
        User user = userRepo.findById(editorManageCatDTO.getUser().getId())
                .orElseThrow(() -> new NullPointerException("No user with id: " + editorManageCatDTO.getUser().getId()));
        Category category = categoryRepo.findById(editorManageCatDTO.getCategory().getId())
                .orElseThrow(() -> new NullPointerException("No category with id: " + editorManageCatDTO.getCategory().getId()));
        if (!user.getRole().name().equals("EDITOR")){
            throw new RuntimeException("Permissions can only be assigned to editors.");
        } else {
            EditorManageCat editorManageCat = new EditorManageCat();
            editorManageCat.setUser(user);
            editorManageCat.setCategory(category);
            editorManageCatRepo.save(editorManageCat);
            return modelMapper.map(editorManageCat, EditorManageCatDTO.class);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public EditorManageCatDTO updateManagement(String id, EditorManageCatDTO editorManageCatDTO) {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteManagement(String id) {
        return null;
    }

    @PreAuthorize("hasAuthority('EDITOR')")
    @Override
    public List<EditorManageCatDTO> findAllManagement() {
        return null;
    }

    @PreAuthorize("hasAuthority('EDITOR')")
    @Override
    public List<ArticleDTO> findDraftByEditor(String editorId) {
        return null;
    }
}
