package org.deco.gachicoding.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    public Long registerTag(String keyword);

    public void registerBoardTag(Long boardIdx, List<String> tags);
}
