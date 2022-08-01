package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.domain.tag.TagRelation;
import org.deco.gachicoding.domain.tag.TagRelationRepository;
import org.deco.gachicoding.domain.tag.TagRepository;
import org.deco.gachicoding.dto.TagResponse;
import org.deco.gachicoding.dto.tag.TagResponseDto;
import org.deco.gachicoding.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagRelationRepository boardTagRepository;

    private Optional<Tag> isDuplicateKeyword(String keyword) {
        Optional<Tag> tag = tagRepository.findByTagKeyword(keyword);
        return tag;
    }

    @Override
    public Tag registerTag(String keyword) {
        Optional<Tag> findTag = isDuplicateKeyword(keyword);

        if(findTag.isPresent()) {
            return findTag.get();
        }

        Tag entity = Tag.builder()
                    .keyword(keyword)
                    .build();

        return tagRepository.save(entity);
    }

    @Override
    public void registerBoardTag(Long articleIdx, List<String> tags, String articleCategory) {
        for (String tag : tags) {
            TagRelation entity = TagRelation.builder()
                    .articleCategory(articleCategory)
                    .articleIdx(articleIdx)
                    .tag(registerTag(tag))
                    .tagKeyword(tag)
                    .build();
            boardTagRepository.save(entity);
        }
    }

    @Override
    public TagResponse getTags(Long articleIdx, String articleCategory, TagResponse dto) {
        List<TagResponseDto> result = new ArrayList<>();
        List<TagRelation> tags = boardTagRepository.findAllByArticleIdxAndArticleCategory(articleIdx, articleCategory);

        for (TagRelation tag : tags) {
            result.add(new TagResponseDto(tag.getTag().getTagKeyword()));
        }

        dto.setTags(result);
        return dto;
    }

    @Transactional
    public void removeBoardTags(Long articleIdx, String type) {
        boardTagRepository.deleteAllByArticleIdxAndArticleCategory(articleIdx, type);
    }
}
