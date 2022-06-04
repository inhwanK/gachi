package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.comment.CommentResponseDto;
import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.deco.gachicoding.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation(value = "댓글 목록")
    @ApiResponses(
            @ApiResponse(code = 200, message = "댓글 목록 반환")
    )
    @GetMapping("/comment/list")
    public Page<CommentResponseDto> getCommentList(@ApiParam(value = "글 카테고리") @RequestParam(value = "articleCategory", defaultValue = "") String articleCategory,
                                                   @ApiParam(value = "글 번호") @RequestParam(value = "articleIdx", defaultValue = "") Long articleIdx,
                                                   @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {

        return commentService.getCommentList(articleCategory, articleIdx, pageable);
    }
}
