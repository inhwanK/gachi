package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.tag.TagResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    public Long registerTag(String keyword);

    public void registerBoardTag(Long boardIdx, List<String> tags, String type);

    public List<TagResponseDto> getTags(Long boardIdx, String type);
}
