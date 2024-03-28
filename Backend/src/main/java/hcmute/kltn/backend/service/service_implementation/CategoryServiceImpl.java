package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.CategoryDTO;

import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        boolean existedCategory = categoryRepo.existsByName(categoryDTO.getName());
        if (existedCategory) {
            throw new RuntimeException("Category with name: " + "'" + categoryDTO.getName() + "'" + " existed.");
        } else {
            Category category = new Category();
            category.setName(categoryDTO.getName());
            if (categoryDTO.getName() != null) {
                Category parentCat = categoryRepo.findById(categoryDTO.getParent().getId())
                        .orElseThrow(() -> new RuntimeException("Parent category not found."));
                category.setParent(parentCat);
            }
            categoryRepo.save(category);
            return modelMapper.map(category, CategoryDTO.class);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteCategory(String id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        categoryRepo.delete(category);
        return "Deleted Successfully";
    }

    @Override
    public CategoryDTO findCategoryById(String id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> allCategories = categoryRepo.findAll();
        return allCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, String id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        boolean existedCategory = categoryRepo.existsByName(categoryDTO.getName());
        if (existedCategory) {
            throw new RuntimeException("The updated category name already exists");
        } else {
            category.setName(categoryDTO.getName());
            if (categoryDTO.getParent() != null) {
                Category parentCat = categoryRepo.findById(categoryDTO.getParent().getId())
                        .orElseThrow(() -> new RuntimeException("Parent category not found."));
                category.setParent(parentCat);
            }
            categoryRepo.save(category);
        }
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findChildCategories(String parentId) {
        Category parentCat = categoryRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found."));
        List<Category> childrenCat = categoryRepo.findChildCategories(parentCat.getId());
        return childrenCat.stream()
                .map(child -> modelMapper.map(child, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> findParentCategories() {
        List<Category> parentCat = categoryRepo.findParentCategories();
        return parentCat.stream()
                .map(parent -> modelMapper.map(parent, CategoryDTO.class))
                .collect(Collectors.toList());
    }
}
