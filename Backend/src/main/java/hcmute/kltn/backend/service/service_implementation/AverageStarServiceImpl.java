package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.AverageStarDTO;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.AverageStar;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.AverageStarRepo;
import hcmute.kltn.backend.service.AverageStarService;
import hcmute.kltn.backend.service.VoteStarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AverageStarServiceImpl implements AverageStarService {
    private final ArticleRepo articleRepo;
    private final AverageStarRepo averageStarRepo;

    @Override
    public Float getAverageStar(String articleId) {
        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("No article with id: " + articleId));

        Float result = (float) 0;
        AverageStar averageStar = averageStarRepo.findByArticle(article);
        if (averageStar != null) {
            return averageStar.getAverageStar();
        } else {
            return result;
        }
    }
}
