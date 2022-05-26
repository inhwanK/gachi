package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
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
public class RestNoticeController {
//    private final NoticeService noticeService;
    private final BoardService boardService;
    private final TagService tagService;
    private final FileService fileService;
    private final static String BOARD_TYPE = "NOTICE";

    @ApiOperation(value = "공지사항 등록")
    @PostMapping("/notice")
    public Long registerNotice(@RequestBody BoardSaveRequestDto dto) {
        log.info("{} Register Controller", "Notice");
        Long noticeIdx = boardService.registerBoard(dto, BOARD_TYPE);

        // if로 검사해도 된다 if (files == null)   익셉션 핸들링 필요
        // try catch 부분(위치)을 바꿔보자
        try {
            log.info("tried File Upload {}", "Notice");
        } catch (NullPointerException e) {
            log.error("{}  Board File Upload Error", "Notice");
            e.printStackTrace();
        }

        try {
            log.info("tried Tag Upload {}", "Notice");
            tagService.registerBoardTag(noticeIdx, dto.getTags(), BOARD_TYPE);
        } catch (NullPointerException e) {
            log.error("{} Board Tags Error", "Notice");
            e.printStackTrace();
        }
        return noticeIdx;
    }

    @ApiOperation(value = "공지사항 리스트 보기")
    @GetMapping("/notice/list")
    public Page<BoardResponseDto> getNoticeList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return boardService.getBoardList(keyword, pageable, BOARD_TYPE);
    }

    @ApiOperation(value = "공지사항 상세 보기")
    @GetMapping("/notice/{boardIdx}")
    public BoardResponseDto getNoticeDetail(@PathVariable Long boardIdx) {
        BoardResponseDto result = boardService.getBoardDetail(boardIdx);
        fileService.getFiles(boardIdx, BOARD_TYPE, result);
        tagService.getTags(boardIdx, BOARD_TYPE, result);
        return result;
    }

    @ApiOperation(value = "공지사항 수정")
    @PutMapping("/notice/modify")
    public BoardResponseDto modifyNotice(@RequestBody BoardUpdateRequestDto dto){
        return boardService.modifyBoard(dto);
    }

    @ApiOperation(value = "공지사항 비활성화")
    @PutMapping("/notice/disable/{boardIdx}")
    public ResponseEntity<ResponseState> disableNotice(@PathVariable Long boardIdx){
        return boardService.disableBoard(boardIdx);
    }

    @ApiOperation(value = "공지사항 활성화")
    @PutMapping("/notice/enable/{boardIdx}")
    public ResponseEntity<ResponseState> enableNotice(@PathVariable Long boardIdx){
        return boardService.enableBoard(boardIdx);
    }

    @ApiOperation(value = "공지사항 삭제")
    @DeleteMapping("/notice/remove/{boardIdx}")
    public ResponseEntity<ResponseState> removeNotice(@PathVariable Long boardIdx){
        return boardService.removeBoard(boardIdx);
    }

}
