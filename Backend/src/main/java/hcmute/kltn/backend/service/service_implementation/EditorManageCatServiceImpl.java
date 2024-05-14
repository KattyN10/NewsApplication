package hcmute.kltn.backend.service.service_implementation;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        boolean checkExists = editorManageCatRepo.existsByCategoryAndUser(category, user);
        if (checkExists) {
            throw new RuntimeException("Data already exists.");
        } else {
            if (!user.getRole().name().equals("EDITOR")) {
                throw new RuntimeException("Permissions can only be assigned to editors.");
            } else {
                EditorManageCat editorManageCat = new EditorManageCat();
                editorManageCat.setUser(user);
                editorManageCat.setCategory(category);
                editorManageCatRepo.save(editorManageCat);
                return modelMapper.map(editorManageCat, EditorManageCatDTO.class);
            }
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public EditorManageCatDTO updateManagement(String id, EditorManageCatDTO editorManageCatDTO) {
        User editor = userRepo.findById(editorManageCatDTO.getUser().getId())
                .orElseThrow(() -> new NullPointerException("No user with id: " + editorManageCatDTO.getUser().getId()));
        Category category = categoryRepo.findById(editorManageCatDTO.getCategory().getId())
                .orElseThrow(() -> new NullPointerException("No category with id: " + editorManageCatDTO.getCategory().getId()));
        EditorManageCat manageCat = editorManageCatRepo.findById(id)
                .orElseThrow(() -> new NullPointerException("No editor manage cat with id: " + id));
        if (!editor.getRole().name().equals("EDITOR")) {
            throw new RuntimeException("Permissions can only be assigned to editors.");
        } else {
            manageCat.setCategory(category);
            manageCat.setUser(editor);
            boolean checkExists = editorManageCatRepo.existsByCategoryAndUser(category, editor);
            if (checkExists) {
                throw new RuntimeException("Data already exists.");
            } else {
                editorManageCatRepo.save(manageCat);
                return modelMapper.map(manageCat, EditorManageCatDTO.class);
            }
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteManagement(String id) {
        EditorManageCat manageCat = editorManageCatRepo.findById(id)
                .orElseThrow(() -> new NullPointerException("No editor manage cat with id: " + id));
        editorManageCatRepo.delete(manageCat);
        return "Delete management successfully.";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public List<EditorManageCatDTO> findAllManagement() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name)
                .orElseThrow(() -> new RuntimeException("User not found."));
        List<EditorManageCat> manageCatList = editorManageCatRepo.findAll();
        return manageCatList.stream()
                .map(manageCat -> modelMapper.map(manageCat, EditorManageCatDTO.class))
                .collect(Collectors.toList());
    }
}
