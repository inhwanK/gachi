package org.deco.gachicoding.post.board.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.application.BoardService;
import org.deco.gachicoding.post.board.presentation.dto.BoardAssembler;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardSaveRequest;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardUpdateRequest;
import org.deco.gachicoding.post.board.presentation.dto.response.BoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(tags = "자유게시판 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private static final String REDIRECT_URL = "/api/board/%d";

    private final BoardService boardService;

    @ApiOperation(value = "게시물 쓰기")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 게시글 번호 반환")
    )
    @PostMapping("/board")
    public ResponseEntity<Void> registerBoard(
            @ApiParam(value = "게시판 요청 body 정보")
            @Valid @RequestBody BoardSaveRequest request
    ) {
        log.info("{} Register Controller", "Board");

        Long boardIdx = boardService.registerBoard(BoardAssembler.boardSaveRequestDto(request));

        String redirectUrl = String.format(REDIRECT_URL, boardIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "게시물 목록")
    @ApiResponses(
            @ApiResponse(code = 200, message = "게시글 목록 반환")
    )
    @GetMapping("/board/list")
    public ResponseEntity<List<BoardResponse>> getBoardList(
            @ApiParam(value = "검색어") @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ApiIgnore @PageableDefault(size = 10) Pageable pageable
    ) {
        BoardListRequestDto dto = BoardAssembler.boardListRequestDto(keyword, pageable);

        List<BoardResponseDto> boardResponseDtos = boardService.getBoardList(dto);

        List<BoardResponse> boardResponses = BoardAssembler.boardResponses(boardResponseDtos);

        return ResponseEntity.ok(boardResponses);
    }

    @ApiOperation(value = "게시물 상세 보기")
    @ApiResponses(
            @ApiResponse(code = 200, message = "게시글 상세 정보 반환")
    )
    @GetMapping("/board/{boardIdx}")
    public ResponseEntity<BoardResponse> getBoardDetail(
            @ApiParam(value = "게시판 번호", example = "1") @PathVariable Long boardIdx
    ) {

        BoardDetailRequestDto dto = BoardAssembler.boardDetailRequestDto(boardIdx);

        BoardResponse boardResponse = BoardAssembler.boardResponse(boardService.getBoardDetail(dto));

        return ResponseEntity.ok(boardResponse);
    }

    @ApiOperation(value = "게시물 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정후 게시글 상세 정보 반환")
    )
    @PutMapping("/board/modify")
    public ResponseEntity<Void> modifyBoard(
            @ApiParam(value = "게시판 수정 요청 body 정보") @RequestBody BoardUpdateRequest request
    ) {

        BoardUpdateRequestDto dto = BoardAssembler.boardUpdateRequestDto(request);

        Long boardIdx = boardService.modifyBoard(dto).getBoardIdx();

        String redirectUrl = String.format(REDIRECT_URL, boardIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "게시물 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/board/disable/{boardIdx}")
    public ResponseEntity<Void> disableBoard(
            @ApiParam(value = "게시판 번호", example = "1") @PathVariable Long boardIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        BoardBasicRequestDto dto = BoardAssembler.boardBasicRequestDto(boardIdx, userEmail);

        boardService.disableBoard(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "게시물 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/board/enable/{boardIdx}")
    public ResponseEntity<Void> enableBoard(
            @ApiParam(value = "게시판 번호", example = "1") @PathVariable Long boardIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {
        BoardBasicRequestDto dto = BoardAssembler.boardBasicRequestDto(boardIdx, userEmail);

        boardService.enableBoard(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "게시물 삭제")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/board/{boardIdx}")
    public ResponseEntity<Void> removeBoard(
            @ApiParam(value = "게시판 번호", example = "1") @PathVariable Long boardIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {
        BoardBasicRequestDto dto = BoardAssembler.boardBasicRequestDto(boardIdx, userEmail);

        boardService.removeBoard(dto);

        return ResponseEntity.noContent().build();
    }
}
