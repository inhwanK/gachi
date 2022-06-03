package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Long registerComment(CommentSaveRequestDto dto);
}
