package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.VoteStarDTO;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.entity.VoteStar;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.repository.VoteStarRepo;
import hcmute.kltn.backend.service.VoteStarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteStarServiceImpl implements VoteStarService {
    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final VoteStarRepo voteStarRepo;
    private final ModelMapper modelMapper;

    @Override
    public VoteStarDTO CUDVote(VoteStarDTO voteStarDTO) {
        Article article = articleRepo.findById(voteStarDTO.getArticle().getId())
                .orElseThrow(() -> new NullPointerException("No article with id: " + voteStarDTO.getArticle().getId()));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        if (voteStarDTO.getStar() > 5 || voteStarDTO.getStar() < 1)
            throw new RuntimeException("Stars must be between 1 and 5");

        VoteStar foundVoteArticle = voteStarRepo.findExistedVote(article.getId(), user.getId());
        VoteStar voteStar = new VoteStar();
        voteStar.setArticle(article);
        voteStar.setUser(user);
        voteStar.setStar(voteStarDTO.getStar());
        if (foundVoteArticle == null) {
            voteStarRepo.save(voteStar);
            return modelMapper.map(voteStar, VoteStarDTO.class);
        } else {
            if (foundVoteArticle.getStar() == voteStarDTO.getStar()) {
                voteStarRepo.delete(foundVoteArticle);
                return null;
            } else {
                foundVoteArticle.setStar(voteStar.getStar());
                voteStarRepo.save(foundVoteArticle);
                return modelMapper.map(foundVoteArticle, VoteStarDTO.class);
            }
        }
    }

    @Override
    public Float getAverageStar(String articleId) {
        List<VoteStar> listVote = voteStarRepo.findListVote(articleId);
        float averageStar = 0 ;
        if (!listVote.isEmpty()){
            for (VoteStar voteStar : listVote) {
                averageStar += voteStar.getStar();
            }
            averageStar /= listVote.size();
        }
        return averageStar;
    }
}
