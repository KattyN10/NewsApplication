package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.SavedArticleDTO;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.SavedArticle;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.SavedArticleRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.SavedArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedArticleServiceImpl implements SavedArticleService {
    private final SavedArticleRepo savedArticleRepo;
    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public SavedArticleDTO addToList(SavedArticleDTO savedArticleDTO) {
        Article article = articleRepo.findById(savedArticleDTO.getArticle().getId())
                .orElseThrow(() -> new NullPointerException("No article with id: " + savedArticleDTO.getArticle().getId()));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        SavedArticle savedArticle = new SavedArticle();
        savedArticle.setArticle(article);
        savedArticle.setUser(user);
        savedArticleRepo.save(savedArticle);
        return modelMapper.map(savedArticle, SavedArticleDTO.class);
    }

    @Override
    public String removeFromList(String id) {
        SavedArticle savedArticle = savedArticleRepo.findById(id)
                .orElseThrow(() -> new NullPointerException("No result were found."));
        try {
            savedArticleRepo.delete(savedArticle);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return "Removed from the saved list";
    }

    @Override
    public List<SavedArticleDTO> findList() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();
        List<SavedArticle> listSavedArticle = savedArticleRepo.findByUserId(user.getId());
        return listSavedArticle.stream()
                .map(article -> modelMapper.map(article, SavedArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SavedArticleDTO findById(String id) {
        SavedArticle savedArticle = savedArticleRepo.findById(id)
                .orElseThrow(() -> new NullPointerException("No result were found"));
        return modelMapper.map(savedArticle, SavedArticleDTO.class);
    }
}
