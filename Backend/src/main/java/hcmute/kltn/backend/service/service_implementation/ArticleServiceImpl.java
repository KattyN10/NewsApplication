package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.AverageStar;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.entity.enum_entity.ArtSource;
import hcmute.kltn.backend.entity.enum_entity.Status;
import hcmute.kltn.backend.entity.enum_entity.UploadPurpose;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.ArticleService;
import hcmute.kltn.backend.service.ImageUploadService;
import hcmute.kltn.backend.service.VoteStarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepo articleRepo;
    private final ImageUploadService imageUploadService;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final VoteStarService voteStarService;

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public ArticleDTO createArticle(MultipartFile file, ArticleRequest articleRequest) {
        Article article = new Article();

        User user = userRepo.findById(articleRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
        Category category = categoryRepo.findById(articleRequest.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        try {
            String imgUrl = imageUploadService.saveImage(file, UploadPurpose.ARTICLE_AVATAR);

            article.setTitle(articleRequest.getTitle());
            article.setAbstracts(articleRequest.getAbstracts());
            article.setContent(articleRequest.getContent());
            article.setCreate_date(LocalDateTime.now());
            article.setReading_time(readingTime(articleRequest.getContent()));
            article.setStatus(Status.DRAFT);
            article.setAvatar(imgUrl);
            article.setArtSource(ArtSource.DEFAULT);
            article.setCategory(category);
            article.setUser(user);

            articleRepo.save(article);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return modelMapper.map(article, ArticleDTO.class);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public String deleteArticle(String id) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found."));
        articleRepo.delete(article);
        return "Deleted Successfully";
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public ArticleDTO updateArticle(String id, MultipartFile file, ArticleRequest articleRequest) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found."));
        User user = userRepo.findById(articleRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
        Category category = categoryRepo.findById(articleRequest.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        if (!file.isEmpty()) {
            try {
                String urlImg = imageUploadService.saveImage(file, UploadPurpose.ARTICLE_AVATAR);
                article.setAvatar(urlImg);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (articleRequest.getTitle() != null) {
            article.setTitle(articleRequest.getTitle());
        }
        if (articleRequest.getAbstracts() != null) {
            article.setAbstracts(articleRequest.getAbstracts());
        }
        if (articleRequest.getContent() != null) {
            article.setContent(articleRequest.getContent());
        }
        article.setReading_time(readingTime(article.getContent()));
        article.setStatus(Status.DRAFT);
        if (user != null) {
            article.setUser(user);
        }
        if (category != null) {
            article.setCategory(category);
        }
        articleRepo.save(article);
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public ArticleDTO findById(String id) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found."));
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public List<ArticleDTO> getTopStarArticle() {
        List<Article> publicArticles = articleRepo.findByStatus(Status.PUBLIC);
        List<Article> result = new ArrayList<>();
        List<AverageStar> averageStarList = new ArrayList<>();

        for (Article publicArticle : publicArticles) {
            float star = voteStarService.getAverageStar(publicArticle.getId());
            AverageStar averageStar = new AverageStar(publicArticle.getId(), star);
            averageStarList.add(averageStar);
        }

        return result.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getTop4NewestArticle() {
        List<Article> publicArticles = articleRepo.findByStatusOrderByCreate_dateDesc(Status.PUBLIC);
        List<Article> result = publicArticles.subList(0, 4);
        return result.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getLatestArtPerCat() {
        List<Article> articleList = articleRepo.findLatestArtPerParentCat();
        return articleList.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    private float readingTime(String content) {
        int count = content.split("\\s+").length;
        int avgReadingSpeed = 200;
        float time = count / avgReadingSpeed;
        return time;
    }

//    @Override
//    public List<ArticleDTO> findByCatId(String id, int page, int size) {
//        Category category = categoryRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found."));
//        List<Article> articleList = articleRepo.findByCatId(category.getId());
//
//        int totalResult = articleList.size();
//        int startIndex = (page - 1) * size;
//        int endIndex = Math.min(startIndex + size, totalResult);
//        if (startIndex > endIndex) {
//            startIndex = endIndex;
//        }
//        List<Article> articleSubList= articleList.subList(startIndex, endIndex);
//        return articleSubList.stream()
//                .map(article -> modelMapper.map(article, ArticleDTO.class))
//                .collect(Collectors.toUnmodifiableList());
//    }

//    @Override
//    public List<ArticleDTO> searchArticle(String key, int page, int size) {
//        return null;
//    }

}
