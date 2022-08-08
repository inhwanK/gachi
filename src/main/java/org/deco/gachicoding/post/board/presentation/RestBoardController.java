package org.deco.gachicoding.post.board.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.ResponseState;
import org.deco.gachicoding.post.board.dto.response.BoardPostResponseDto;
import org.deco.gachicoding.post.board.dto.request.BoardSaveRequestDto;
import org.deco.gachicoding.post.board.dto.request.BoardUpdateRequestDto;
import org.deco.gachicoding.post.board.application.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "자유게시판 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestBoardController {
    private final BoardService boardService;

    @ApiOperation(value = "자유게시판 게시글 쓰기")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 게시글 번호 반환")
    )
    @PostMapping("/board")
    public Long registerBoard(@ApiParam(value = "게시판 요청 body 정보") @RequestBody BoardSaveRequestDto dto) throws Exception {
            log.info("{} Register Controller", "Board");

            return boardService.registerBoard(dto);
    }

    @ApiOperation(value = "자유게시판 게시글 목록")
    @ApiResponses(
            @ApiResponse(code = 200, message = "게시글 목록 반환")
    )
    @GetMapping("/board/list")
    public Page<BoardPostResponseDto> getBoardList(@ApiParam(value = "검색어") @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                   @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {

        return boardService.getBoardList(keyword, pageable);
    }

    @ApiOperation(value = "자유게시판 상세 게시글")
    @ApiResponses(
            @ApiResponse(code = 200, message = "게시글 상세 정보 반환")
    )
    @GetMapping("/board/{boardIdx}")
    public BoardPostResponseDto getBoardDetail(@ApiParam(value = "게시판 번호") @PathVariable Long boardIdx) {

        return boardService.getBoardDetail(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 수정 (리팩토링 필요함)")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정후 게시글 상세 정보 반환")
    )
    @PutMapping("/board/modify")
    public BoardPostResponseDto modifyBoard(@ApiParam(value = "게시판 수정 요청 body 정보") @RequestBody BoardUpdateRequestDto dto) {
        return boardService.modifyBoard(dto);
    }

    @ApiOperation(value = "자유게시판 게시글 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/board/disable/{boardIdx}")
    public ResponseEntity<ResponseState> disableBoard(@ApiParam(value = "게시판 번호") @PathVariable Long boardIdx) {
        return boardService.disableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/board/enable/{boardIdx}")
    public ResponseEntity<ResponseState> enableBoard(@ApiParam(value = "게시판 번호") @PathVariable Long boardIdx) {
        return boardService.enableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 삭제")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/board/{boardIdx}")
    public ResponseEntity<ResponseState> removeBoard(@ApiParam(value = "게시판 번호") @PathVariable Long boardIdx) {
        return boardService.removeBoard(boardIdx);
    }
}
