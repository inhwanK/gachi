package org.deco.gachicoding.controller;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
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

    @GetMapping("/board/list")
    public Page<BoardResponseDto> getBoardList(@PageableDefault(size = 10) Pageable pageable){
        return boardService.getBoardList(pageable);
    }

    @GetMapping("/board/{boardIdx}")
    public BoardResponseDto getBoardDetail(@PathVariable Long boardIdx){
        return boardService.getBoardDetail(boardIdx);
    }

    @PostMapping("/board")
    public Long registerBoard(@RequestBody BoardSaveRequestDto dto){
        return boardService.registerBoard(dto);
    }

    @DeleteMapping("/board/{boardIdx}")
    public Long removeBoard(@PathVariable Long boardIdx){
        return boardService.removeBoard(boardIdx);
    }
}
