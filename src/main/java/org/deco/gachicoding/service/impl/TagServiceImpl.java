package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.tag.BoardTag;
import org.deco.gachicoding.domain.tag.BoardTagRepository;
import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.domain.tag.TagRepository;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.TagResponse;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.tag.TagResponseDto;
import org.deco.gachicoding.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.DATA_NOT_EXIST;
import static org.deco.gachicoding.dto.response.StatusEnum.REMOVE_SUCCESS;

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
    public void registerBoardTag(Long boardIdx, List<String> tags, String type) {
        for (String tag : tags) {
            BoardTag entity = BoardTag.builder()
                    .boardType(type)
                    .boardIdx(boardIdx)
                    .tag(registerTag(tag))
                    .tagKeyword(tag)
                    .build();
            boardTagRepository.save(entity);
        }
    }

    @Override
    public TagResponse getTags(Long boardIdx, String type, TagResponse dto) {
        List<TagResponseDto> result = new ArrayList<>();
        List<BoardTag> tags = boardTagRepository.findAllByBoardIdxAndBoardType(boardIdx, type);

        for (BoardTag tag : tags) {
            result.add(new TagResponseDto(tag.getTag().getTagKeyword()));
        }

        dto.setTags(result);
        return dto;
    }

    @Transactional
    public void removeBoardTags(Long boardIdx, String type) {
        boardTagRepository.deleteAllByBoardIdxAndAndBoardType(boardIdx, type);
    }
}
