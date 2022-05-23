package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    public Long registerTag(String keyword);

    public void registerBoardTag(Long boardIdx, List<String> tags, String type);

    public ResponseDto getTags(Long boardIdx, String type, ResponseDto dto);
}
