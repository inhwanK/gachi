package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
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
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "공지사항 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {

    private final BoardService boardService;
    private final TagService tagService;
    private final FileService fileService;
    private final static String BOARD_TYPE = "NOTICE";

    @ApiOperation(value = "공지사항 등록", notes = "게시판 요청 DTO를 받아 공지사항 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 공지사항 번호 반환")
    )
    @PostMapping("/notice")
    public Long registerNotice(@ApiParam(name = "게시판 요청 DTO", value = "게시판 요청 body 정보") @RequestBody BoardSaveRequestDto dto) {
        log.info("{} Register Controller", "Notice");
        Long noticeIdx = boardService.registerBoard(dto, BOARD_TYPE);

        try {
            log.info("tried Tag Upload {}", "Notice");
            tagService.registerBoardTag(noticeIdx, dto.getTags(), BOARD_TYPE);
        } catch (NullPointerException e) {
            log.error("{} Board Tags Error", "Notice");
            e.printStackTrace();
        }
        return noticeIdx;
    }

    @ApiOperation(value = "공지사항 리스트 보기", notes = "공지사항 목록을 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 목록 반환")
    )
    @GetMapping("/notice/list")
    public Page<BoardResponseDto> getNoticeList(@ApiParam(name = "검색어") @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {
        Page<BoardResponseDto> result = boardService.getBoardList(keyword, pageable, BOARD_TYPE);
        // 리팩토링 중복 코드 제거
        // 리팩토링 글 삭제시 관련 태그 삭제 (-> db테이블 생성시 설정 해주면 될듯)
        result.forEach(
                BoardResponseDto ->
                        tagService.getTags(BoardResponseDto.getBoardIdx(), BOARD_TYPE, BoardResponseDto)
        );
        return result;
    }

    @ApiOperation(value = "공지사항 상세 보기", notes = "상세한 공지사항 데이터 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 상세 정보 반환")
    )
    @GetMapping("/notice/{boardIdx}")
    public BoardResponseDto getNoticeDetail(@ApiParam(name = "게시판 번호") @PathVariable Long boardIdx) {
        BoardResponseDto result = boardService.getBoardDetail(boardIdx);
        fileService.getFiles(boardIdx, BOARD_TYPE, result);
        tagService.getTags(boardIdx, BOARD_TYPE, result);
        return result;
    }

    @ApiOperation(value = "공지사항 수정", notes = "공지사항 등록 수행 (리팩토링 필요함)")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 후 공지사항 상세 정보 반환")
    )
    @PutMapping("/notice/modify")
    public BoardResponseDto modifyNotice(@ApiParam(name = "게시판 수정 DTO", value = "게시판 수정 요청 body 정보") @RequestBody BoardUpdateRequestDto dto) {
        return boardService.modifyBoard(dto);
    }

    @ApiOperation(value = "공지사항 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/notice/disable/{boardIdx}")
    public ResponseEntity<ResponseState> disableNotice(@ApiParam(name = "공지사항 번호") @PathVariable Long boardIdx) {
        return boardService.disableBoard(boardIdx);
    }

    @ApiOperation(value = "공지사항 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/notice/enable/{boardIdx}")
    public ResponseEntity<ResponseState> enableNotice(@ApiParam(name = "공지사항 번호") @PathVariable Long boardIdx) {
        return boardService.enableBoard(boardIdx);
    }

    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 번호를 받아 공지사항 삭제 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/notice/remove/{boardIdx}")
    public ResponseEntity<ResponseState> removeNotice(@ApiParam(name = "공지사항 번호") @PathVariable Long boardIdx) {
        return boardService.removeBoard(boardIdx);
    }

}
