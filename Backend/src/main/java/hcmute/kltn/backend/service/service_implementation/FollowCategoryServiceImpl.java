package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.FollowCategoryDTO;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.FollowCategory;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.entity.enum_entity.Status;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.repository.FollowCategoryRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.FollowCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowCategoryServiceImpl implements FollowCategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final FollowCategoryRepo followCategoryRepo;
    private final ArticleRepo articleRepo;

    @Override
    public FollowCategoryDTO createFollow(FollowCategoryDTO followCategoryDTO) {
        Category category = categoryRepo.findById(followCategoryDTO.getCategory().getId())
                .orElseThrow(() -> new NullPointerException("No category with id: " + followCategoryDTO.getCategory().getId()));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        FollowCategory followCategory = new FollowCategory();
        followCategory.setCategory(category);
        followCategory.setUser(user);
        followCategoryRepo.save(followCategory);

        List<Category> childCategory = categoryRepo.findChildCategories(category.getId());
        if (!childCategory.isEmpty()) {
            for (Category childCat : childCategory) {
                FollowCategory tempFollow = new FollowCategory();
                tempFollow.setUser(user);
                tempFollow.setCategory(childCat);
                followCategoryRepo.save(tempFollow);
            }
        }
        return modelMapper.map(followCategory, FollowCategoryDTO.class);
    }

    @Override
    public String removeFollow(String categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NullPointerException("No category with id: " + categoryId));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        FollowCategory followCategory = followCategoryRepo.findByUserAndCategory(user, category);
        if (followCategory == null) {
            throw new RuntimeException("Invalid user or category");
        } else {
            followCategoryRepo.delete(followCategory);

            List<Category> childCategory = categoryRepo.findChildCategories(category.getId());
            if (!childCategory.isEmpty()) {
                for (Category childCat : childCategory) {
                    FollowCategory followChildCategory = followCategoryRepo.findByUserAndCategory(user, childCat);
                    if (followChildCategory != null) {
                        followCategoryRepo.delete(followChildCategory);
                    }
                }
                return "Successfully unfollowed parent category: " + category.getName() + " and children categories.";
            } else {
                return "Successfully unfollowed category: " + category.getName() + ".";
            }
        }
    }

    @Override
    public List<ArticleDTO> getFollowedArticle() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        List<Article> articleList = new ArrayList<>();
        List<FollowCategory> followCategoryList = followCategoryRepo.findByUser(user);
        for (FollowCategory followCat : followCategoryList) {
            List<Article> tempArticle = articleRepo.findByCategoryAndStatus(followCat.getCategory(),
                    Status.PUBLIC);
            articleList.addAll(tempArticle);
        }
        Collections.sort(articleList, Comparator.comparing(Article::getCreate_date, Comparator.reverseOrder()));

        return articleList.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }
}
