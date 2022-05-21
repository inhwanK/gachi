package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.domain.tag.TagRepository;
import org.deco.gachicoding.dto.tag.TagSaveRequestDto;
import org.deco.gachicoding.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    private Optional<Tag> isDuplicateKeyword(String keyword) {
        Optional<Tag> tag = tagRepository.findByTagKeyword(keyword);
        return tag;
    }

    @Override
    public Long registerTag(TagSaveRequestDto dto) {
        Optional<Tag> findTag = isDuplicateKeyword(dto.getKeyword());

        if(findTag.isPresent()) {
            return findTag.get().getTagIdx();
        }

        return tagRepository.save(dto.toEntity()).getTagIdx();
    }
}
