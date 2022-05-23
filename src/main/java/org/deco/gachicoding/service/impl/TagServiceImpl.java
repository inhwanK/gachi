package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.tag.BoardTag;
import org.deco.gachicoding.domain.tag.BoardTagRepository;
import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.domain.tag.TagRepository;
import org.deco.gachicoding.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final BoardTagRepository boardTagRepository;

    private Optional<Tag> isDuplicateKeyword(String keyword) {
        Optional<Tag> tag = tagRepository.findByTagKeyword(keyword);
        return tag;
    }

    @Override
    public Long registerTag(String keyword) {
        Optional<Tag> findTag = isDuplicateKeyword(keyword);

        if(findTag.isPresent()) {
            return findTag.get().getTagIdx();
        }

        Tag entity = Tag.builder()
                    .keyword(keyword)
                    .build();

        return tagRepository.save(entity).getTagIdx();
    }

    @Override
    public void registerBoardTag(Long boardIdx, List<String> tags, String type) {
        for (String tag : tags) {
            BoardTag entity = BoardTag.builder()
                    .boardType(type)
                    .boardIdx(boardIdx)
                    .tagIdx(registerTag(tag))
                    .tagKeyword(tag)
                    .build();
            boardTagRepository.save(entity);
        }
    }
}
