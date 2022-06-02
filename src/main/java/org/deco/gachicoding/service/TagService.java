package org.deco.gachicoding.service;

import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.TagResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    Tag registerTag(String keyword);

    void registerBoardTag(Long boardIdx, List<String> tags, String type);

    TagResponse getTags(Long boardIdx, String type, TagResponse dto);

    void removeBoardTags(Long boardIdx, String type);
}
