package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.service.BoardService;
import org.deco.gachicoding.service.FileService;
import org.deco.gachicoding.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestBoardController {
    private final BoardService boardService;
    private final FileService fileService;
    private final TagService tagService;
    private final String BOARD_TYPE = "BOARD";

    @ApiOperation(value = "자유게시판 게시글 쓰기")
    @PostMapping("/board")
    public Long registerBoard(@RequestBody BoardSaveRequestDto dto) {
        log.info("{} Register Controller", "Board");
        Long boardIdx = boardService.registerBoard(dto, BOARD_TYPE);

        // if로 검사해도 된다 if (files == null)   익셉션 핸들링 필요
        try {
//            s3Service.uploadRealImg(boardIdx, dto.getFiles(), dto.getBoardType());
            tagService.registerBoardTag(boardIdx, dto.getTags(), BOARD_TYPE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return boardIdx;
    }

    @ApiOperation(value = "자유게시판 게시글 목록")
    @GetMapping("/board/list")
    public Page<BoardResponseDto> getBoardList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable, @RequestParam String boardType){
        return boardService.getBoardList(keyword, pageable, BOARD_TYPE);
    }

    @ApiOperation(value = "자유게시판 상세 게시글")
    @GetMapping("/board/{boardIdx}")
    public BoardResponseDto getBoardDetail(@PathVariable Long boardIdx){
        BoardResponseDto result = boardService.getBoardDetail(boardIdx);
        fileService.getFiles(boardIdx, result.getBoardType(), result);
        tagService.getTags(boardIdx, result.getBoardType(), result);
        return result;
    }

    @ApiOperation(value = "자유게시판 게시글 수정")
    @PutMapping("/board/modify")
    public BoardResponseDto modifyBoard(@RequestBody BoardUpdateRequestDto dto){
        return boardService.modifyBoard(dto);
    }

    @ApiOperation(value = "자유게시판 게시글 비활성화")
    @PutMapping("/board/disable/{boardIdx}")
    public ResponseEntity<ResponseState> disableBoard(@PathVariable Long boardIdx){
        return boardService.disableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 활성화")
    @PutMapping("/board/enable/{boardIdx}")
    public ResponseEntity<ResponseState> enableBoard(@PathVariable Long boardIdx){
        return boardService.enableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 삭제")
    @DeleteMapping("/board/{boardIdx}")
    public ResponseEntity<ResponseState> removeBoard(@PathVariable Long boardIdx){
        return boardService.removeBoard(boardIdx);
    }
}
