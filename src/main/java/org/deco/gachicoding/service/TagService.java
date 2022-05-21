package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.tag.TagSaveRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    public Long registerTag(TagSaveRequestDto dto);

}
