package org.deco.gachicoding.post.notice.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.ResponseState;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeBasicRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeUpdateRequestDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeUpdateResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "공지사항 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 등록", notes = "공지사항 작성 요청 DTO를 받아 공지사항 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 공지사항 번호 반환")
    )
    @PostMapping("/notice")
    public Long registerNotice(@ApiParam(value = "공지사항 요청 body 정보") @RequestBody NoticeSaveRequestDto dto) throws Exception {
        log.info("{} Register Controller", "Notice");

        return noticeService.registerNotice(dto);
    }

    @ApiOperation(value = "공지사항 리스트 보기", notes = "공지사항 목록을 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 목록 반환")
    )
    @GetMapping("/notice/list")
    public List<NoticeResponseDto> getNoticeList(@ApiParam(value = "keyword") @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                 @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {

        return noticeService.getNoticeList(keyword, pageable);
    }

    @ApiOperation(value = "공지사항 상세 보기", notes = "상세한 공지사항 데이터 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 상세 정보 반환")
    )
    @GetMapping("/notice/{notIdx}")
    public NoticeResponseDto getNoticeDetail(@ApiParam(value = "게시판 번호") @PathVariable Long notIdx) {

        return noticeService.getNoticeDetail(notIdx);
    }

    @ApiOperation(value = "공지사항 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 후 공지사항 상세 정보 반환")
    )
    @PutMapping("/notice/modify")
    public NoticeUpdateResponseDto modifyNotice(@ApiParam(value = "게시판 수정 요청 body 정보") @RequestBody NoticeUpdateRequestDto dto) {
        return noticeService.modifyNotice(dto);
    }

    @ApiOperation(value = "공지사항 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/notice/disable")
    public ResponseEntity<ResponseState> disableNotice(@ApiParam(value = "공지사항 기본 요청 DTO") @RequestBody NoticeBasicRequestDto dto) {
        return noticeService.disableNotice(dto);
    }

    @ApiOperation(value = "공지사항 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/notice/enable")
    public ResponseEntity<ResponseState> enableNotice(@ApiParam(value = "공지사항 기본 요청 DTO") @RequestBody NoticeBasicRequestDto dto) {
        return noticeService.enableNotice(dto);
    }

//    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 번호를 받아 공지사항 삭제 수행")
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "삭제 성공")
//    )
//    @DeleteMapping("/notice/remove")
//    public ResponseEntity<ResponseState> removeNotice(@ApiParam(value = "공지사항 기본 요청 DTO") @RequestBody NoticeBasicPostRequestDto dto) {
//        return noticeService.removeNotie(dto);
//    }

}
