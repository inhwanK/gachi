package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.service.board.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestBoardController {

    private final BoardService boardService;

    @ApiOperation(value = "자유게시판 게시글 목록")
    @GetMapping("/board/list")
    public Page<BoardResponseDto> getBoardList(@PageableDefault(size = 10) Pageable pageable){
        return boardService.getBoardList(pageable);
    }

    @ApiOperation(value = "자유게시판 상세 게시글")
    @GetMapping("/board/{boardIdx}")
    public BoardResponseDto getBoardDetail(@PathVariable Long boardIdx){
        return boardService.getBoardDetail(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 쓰기")
    @PostMapping("/board")
    public Long registerBoard(@RequestBody BoardSaveRequestDto dto){
        return boardService.registerBoard(dto);
    }

    @ApiOperation(value = "자유게시판 게시글 수정")
    @PutMapping("/board/modify")
    public BoardResponseDto modifyBoard(@RequestBody BoardUpdateRequestDto dto){
        return boardService.modifyBoard(dto);
    }

    @ApiOperation(value = "자유게시판 게시글 비활성화")
    @PutMapping("/board/disable/{boardIdx}")
    public void disableBoard(@PathVariable Long boardIdx){
        boardService.disableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 활성화")
    @PutMapping("/board/enable/{boardIdx}")
    public void enableBoard(@PathVariable Long boardIdx){
        boardService.enableBoard(boardIdx);
    }

    @ApiOperation(value = "자유게시판 게시글 삭제")
    @DeleteMapping("/board/{boardIdx}")
    public void removeBoard(@PathVariable Long boardIdx){
        boardService.removeBoard(boardIdx);
    }
}
