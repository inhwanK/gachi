package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.comment.CommentRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.service.CommentService;
import org.springframework.stereotype.Service;

import static org.deco.gachicoding.dto.response.StatusEnum.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Override
    public Long registerComment(CommentSaveRequestDto dto) {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return commentRepository.save(dto.toEntity(writer)).getCommIdx();
    }
}
