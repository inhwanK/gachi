package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.service.BoardService;
import org.deco.gachicoding.service.TagService;
import org.deco.gachicoding.service.impl.FileServiceImpl;
import org.deco.gachicoding.service.impl.S3ServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestBoardController {
    private final BoardService boardService;
    private final FileServiceImpl fileService;
    private final S3ServiceImpl s3Service;
    private final TagService tagService;

    private final String type = "board";

    @ApiOperation(value = "자유게시판 게시글 쓰기")
    @PostMapping("/board")
    public Long registerBoard(@RequestBody BoardSaveRequestDto dto) {
        Long boardIdx = boardService.registerBoard(dto);

        // if로 검사해도 된다 if (files == null)   익셉션 핸들링 필요
        try {
            s3Service.uploadRealImg(dto.getFiles(), boardIdx, type);
            tagService.registerBoardTag(boardIdx, dto.getTags(), type);
        } catch (IOException | NullPointerException | URISyntaxException e) {
            e.printStackTrace();
        }

        return boardIdx;
    }

    @ApiOperation(value = "자유게시판 게시글 목록")
    @GetMapping("/board/list")
    public Page<BoardResponseDto> getBoardList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable){
        return boardService.getBoardList(keyword, pageable);
    }

    @ApiOperation(value = "자유게시판 상세 게시글")
    @GetMapping("/board/{boardIdx}")
    public BoardResponseDto getBoardDetail(@PathVariable Long boardIdx){
        BoardResponseDto result = boardService.getBoardDetail(boardIdx);
        result.setFileList(fileService.getFileList("board", boardIdx));
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
