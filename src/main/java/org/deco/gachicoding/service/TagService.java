package org.deco.gachicoding.service;

import org.deco.gachicoding.domain.tag.Tag;
import org.deco.gachicoding.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    Tag registerTag(String keyword);

    void registerBoardTag(Long boardIdx, List<String> tags, String type);

    ResponseDto getTags(Long boardIdx, String type, ResponseDto dto);
}
