package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.comment.CommentResponseDto;
import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.deco.gachicoding.dto.comment.CommentUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Long registerComment(CommentSaveRequestDto dto);

    Page<CommentResponseDto> getCommentList(String articleCategory, Long articleIdx, Pageable pageable);

    CommentResponseDto modifyComment(CommentUpdateRequestDto dto);

    ResponseEntity<ResponseState> disableComment(Long commentIdx);

    ResponseEntity<ResponseState> enableComment(Long commentIdx);

    ResponseEntity<ResponseState> removeComment(Long commentIdx);
}
