package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.deco.gachicoding.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "댓글 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestCommentComtroller {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 댓글 번호 반환")
    )
    @PostMapping("/comment")
    public Long registerComment(@ApiParam(value = "댓글 요청 body 정보") @RequestBody CommentSaveRequestDto dto) {
        log.info("{} Register Controller", "Comment");

        return commentService.registerComment(dto);
    }
}
