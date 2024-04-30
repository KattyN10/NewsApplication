package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.TagDTO;
import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.entity.Tag;
import hcmute.kltn.backend.repository.TagRepo;
import hcmute.kltn.backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepo tagRepo;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        boolean existedTag = tagRepo.existsByValue(tagDTO.getValue());
        if (existedTag) {
            throw new RuntimeException("Tag: " + tagDTO.getValue() + "existed.");
        } else {
            tag.setValue(tagDTO.getValue());
            tagRepo.save(tag);
        }
        return modelMapper.map(tag, TagDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteTag(String id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found."));
        tagRepo.delete(tag);
        return "Deleted successfully";
    }

    @Override
    public TagDTO findTagById(String id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        return modelMapper.map(tag, TagDTO.class);
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> allTags = tagRepo.findAll();
        return allTags.stream()
                .map(tag -> modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public TagDTO updateTag(TagDTO tagDTO, String id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found."));
        boolean existedTag = tagRepo.existsByValue(tagDTO.getValue());
        if (existedTag) {
            throw new RuntimeException("The updated tag value already exists");
        } else {
            tag.setValue(tagDTO.getValue());
            tagRepo.save(tag);
        }
        return modelMapper.map(tag, TagDTO.class);
    }
}
