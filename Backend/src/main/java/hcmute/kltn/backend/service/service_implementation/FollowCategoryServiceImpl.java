package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.FollowCategoryDTO;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.FollowCategory;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.repository.FollowCategoryRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.FollowCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowCategoryServiceImpl implements FollowCategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final FollowCategoryRepo followCategoryRepo;

    @Override
    public FollowCategoryDTO createFollow(FollowCategoryDTO followCategoryDTO) {
        Category category = categoryRepo.findById(followCategoryDTO.getCategory().getId())
                .orElseThrow(() -> new NullPointerException("No category with id: " + followCategoryDTO.getCategory().getId()));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

//        List<Category> childCategory = categoryRepo.findChildCategories(category.getId());
//        if (childCategory.isEmpty()) {
//
//        }

        FollowCategory followCategory = new FollowCategory();
        followCategory.setCategory(category);
        followCategory.setUser(user);
        followCategoryRepo.save(followCategory);

        return modelMapper.map(followCategory, FollowCategoryDTO.class);
    }

    @Override
    public String removeFollow(String categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NullPointerException("No category with id: " + categoryId));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        FollowCategory followCategory = followCategoryRepo.findByUserAndCategory(user, category)
                .orElseThrow(() -> new NullPointerException("Invalid user or category."));
        followCategoryRepo.delete(followCategory);
        return "Successfully unfollowed category: " + category.getName();
    }
}