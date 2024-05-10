package hcmute.kltn.backend.service.service_implementation;

import com.darkprograms.speech.translator.GoogleTranslate;
import hcmute.kltn.backend.dto.ArticleDTO;
import hcmute.kltn.backend.dto.AverageStar;
import hcmute.kltn.backend.dto.request.ArticleRequest;
import hcmute.kltn.backend.dto.request.TagArticleRequest;
import hcmute.kltn.backend.entity.*;
import hcmute.kltn.backend.entity.enum_entity.ArtSource;
import hcmute.kltn.backend.entity.enum_entity.Status;
import hcmute.kltn.backend.entity.enum_entity.UploadPurpose;
import hcmute.kltn.backend.repository.*;
import hcmute.kltn.backend.service.ArticleService;
import hcmute.kltn.backend.service.ImageUploadService;
import hcmute.kltn.backend.service.VoteStarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final TagArticleRepo tagArticleRepo;
    private final TagRepo tagRepo;

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public ArticleDTO createArticle(MultipartFile file, ArticleRequest articleRequest,
                                    TagArticleRequest tagArticleRequest) {
        Article article = new Article();
        Category category = categoryRepo.findById(articleRequest.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        try {
            String imgUrl = imageUploadService.saveImage(file, UploadPurpose.ARTICLE_AVATAR);
            article.setTitle(articleRequest.getTitle());
            article.setAbstracts(articleRequest.getAbstracts());
            article.setContent(articleRequest.getContent());
            article.setCreate_date(LocalDateTime.now());
            article.setReading_time(readingTime(articleRequest.getContent()));
            article.setStatus(Status.DRAFT);
            article.setAvatar(imgUrl);
            article.setArtSource(ArtSource.PQ_EXPRESS);
            article.setCategory(category);
            article.setUser(user);

            List<Tag> tagList = new ArrayList<>();
            for (Tag tag : tagArticleRequest.getTagList()) {
                Tag foundTag = tagRepo.findById(tag.getId())
                        .orElseThrow(() -> new NullPointerException("No tag with id: " + tag.getId()));
                tagList.add(foundTag);
            }
            articleRepo.save(article);
            for (Tag tag : tagList) {
                TagArticle tagArticle = new TagArticle();
                tagArticle.setArticle(article);
                tagArticle.setTag(tag);
                tagArticleRepo.save(tagArticle);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return modelMapper.map(article, ArticleDTO.class);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public String deleteArticle(String id) {
        // Writer chỉ xóa được bài DRAFT của mình
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found."));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();
        if (user != article.getUser()) {
            throw new RuntimeException("Other users' posts cannot be deleted.");
        } else {
            if (article.getStatus() == Status.PUBLIC) {
                throw new RuntimeException("You cannot delete public posts.");
            } else {
                List<TagArticle> tagArticleList = tagArticleRepo.findByArticle(article);
                if (!tagArticleList.isEmpty()) {
                    for (TagArticle tagArticle : tagArticleList) {

                        List<TagArticle> tagArticleListByTag = tagArticleRepo.findByTag(tagArticle.getTag());
                        if (tagArticleListByTag.size() == 1) {
                            Tag tag = tagRepo.findByValue(tagArticle.getTag().getValue());
                            tagArticleRepo.delete(tagArticle);
                            tagRepo.delete(tag);
                        } else {
                            tagArticleRepo.delete(tagArticle);
                        }
                    }
                }
            }
            articleRepo.delete(article);
        }
        return "Deleted Successfully.";
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Override
    public ArticleDTO updateArticle(String id, MultipartFile file, ArticleRequest articleRequest,
                                    TagArticleRequest tagArticleRequest) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new NullPointerException("Article not found."));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();
        Category category = categoryRepo.findById(articleRequest.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        if (user != article.getUser()) {
            throw new RuntimeException("Other users' posts cannot be updated.");
        } else {
            try {
                String urlImg = imageUploadService.saveImage(file, UploadPurpose.ARTICLE_AVATAR);
                article.setAvatar(urlImg);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
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
            if (category != null) {
                article.setCategory(category);
            }
            article.setStatus(Status.DRAFT);
            // update tag
            if (!tagArticleRequest.getTagList().isEmpty()) {
                // delete old tags
                List<TagArticle> tagArticleList = tagArticleRepo.findByArticle(article);
                tagArticleRepo.deleteAll(tagArticleList);
                // insert new tags
                List<Tag> tagList = new ArrayList<>();
                for (Tag tag : tagArticleRequest.getTagList()) {
                    Tag foundTag = tagRepo.findById(tag.getId())
                            .orElseThrow(() -> new RuntimeException("No tag with id: " + tag.getId()));
                    tagList.add(foundTag);
                }
                for (Tag tag : tagList) {
                    TagArticle tagArticle = new TagArticle();
                    tagArticle.setArticle(article);
                    tagArticle.setTag(tag);
                    tagArticleRepo.save(tagArticle);
                }
            }
            articleRepo.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }

    }

    @Override
    public ArticleDTO findById(String id) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found."));
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public List<ArticleDTO> getTop3StarArticle() {
        List<Article> publicArticles = articleRepo.findByStatus(Status.PUBLIC);
        List<AverageStar> averageStarList = new ArrayList<>();
        List<Article> result = new ArrayList<>();
        for (Article publicArticle : publicArticles) {
            float star = voteStarService.getAverageStar(publicArticle.getId());
            AverageStar averageStar = new AverageStar(publicArticle.getId(), star);
            averageStarList.add(averageStar);
        }
        for (AverageStar averageStar : averageStarList) {
            Article newArticle = articleRepo.findById(averageStar.getId()).orElseThrow();
            result.add(newArticle);
        }

        return result.subList(0, 3).stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getTop5NewestArticle() {
        List<Article> publicArticles = articleRepo.findByStatusOrderByCreate_dateDesc(Status.PUBLIC);
        List<Article> result = publicArticles.subList(0, 5);
        return result.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getLatestArtPer4Cat() {
        List<Category> categoryList = categoryRepo.find4ParentCatHaveMaxArticle();
        List<Article> result = new ArrayList<>();
        for (Category category : categoryList) {
            Article article = articleRepo.findLatestArtPerCat(category.getId());
            result.add(article);
        }
        return result.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getTop6ReactArt() {
        List<Article> articleList = articleRepo.findMostReactArticle();
        return articleList.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getLatestByVnExpress() {
        List<Article> articleList = articleRepo.findByArtSourceOrderByCreate_dateDesc(ArtSource.VN_EXPRESS);
        return articleList.subList(0, 6).stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getLatestByDanTri() {
        List<Article> articleList = articleRepo.findByArtSourceOrderByCreate_dateDesc(ArtSource.DAN_TRI);
        return articleList.subList(0, 5).stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getRandomArtSameCat(String catId) {
        List<Article> articleList = articleRepo.findByCatId(catId);
        Collections.shuffle(articleList);
        if (articleList.size() < 5) {
            return articleList.stream()
                    .map(article -> modelMapper.map(article, ArticleDTO.class))
                    .collect(Collectors.toList());
        } else {
            return articleList.subList(0, 5).stream()
                    .map(article -> modelMapper.map(article, ArticleDTO.class))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public List<ArticleDTO> searchArticle(String keyword) {
        try {
            String language = GoogleTranslate.detectLanguage(keyword);
            if (Objects.equals(language, "en")) {
                keyword = GoogleTranslate.translate("vi", keyword);
            }
            List<Article> articleList = articleRepo.searchArticle(keyword);
            return articleList.stream()
                    .map(article -> modelMapper.map(article, ArticleDTO.class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Float readingTime(String content) {
        int count = content.split("\\s+").length;
        int avgReadingSpeed = 200;
        return (float) (count / avgReadingSpeed);

    }

}
