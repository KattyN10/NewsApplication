package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.TagArticleDTO;
import hcmute.kltn.backend.entity.TagArticle;
import hcmute.kltn.backend.repository.TagArticleRepo;
import hcmute.kltn.backend.service.TagArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagArticleServiceImpl implements TagArticleService {
    private final TagArticleRepo tagArticleRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<TagArticleDTO> getTagsOfArticle(String articleId) {
        List<TagArticle> tags = tagArticleRepo.findByArticle_Id(articleId);
        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagArticleDTO.class))
                .collect(Collectors.toList());
    }
}
