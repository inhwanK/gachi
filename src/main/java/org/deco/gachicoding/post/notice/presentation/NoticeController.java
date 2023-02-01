package org.deco.gachicoding.post.notice.presentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeBasicRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeDetailRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeListRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeUpdateRequestDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.presentation.dto.NoticeAssembler;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(tags = "공지사항 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NoticeController {

    private static final String REDIRECT_URL = "/api/notice/%d";

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 등록", notes = "공지사항 작성 요청 DTO를 받아 공지사항 등록 수행")
    @ApiResponse(code = 200, message = "등록된 공지사항 번호 반환")
    @PostMapping("/notice")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Void> registerNotice(
            @ApiParam(value = "공지사항 요청 body 정보")
            @RequestBody @Valid NoticeSaveRequest request
    ) {
        log.info("{} Register Controller", "Notice");

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Long notIdx = noticeService.registerNotice(
                userEmail,
                NoticeAssembler.noticeSaveRequestDto(request)
        );

        String redirectUrl = String.format(REDIRECT_URL, notIdx);

        // 이 부분 더 알아보자 리다이렉트 부분
        return ResponseEntity
                .created(URI.create(redirectUrl))
                .build();
    }

    @ApiOperation(value = "공지사항 리스트 보기", notes = "공지사항 목록을 응답")
    @ApiResponse(code = 200, message = "공지사항 목록 반환")
    @GetMapping("/notice/list")
    public ResponseEntity<List<NoticeResponse>> getNoticeList(
            @ApiParam(value = "keyword") @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ApiIgnore @PageableDefault(size = 10) Pageable pageable
    ) {
        NoticeListRequestDto dto = NoticeAssembler.noticeListRequestDto(keyword, pageable);

        List<NoticeResponseDto> noticeResponseDtos = noticeService.getNoticeList(dto);

        List<NoticeResponse> noticeResponses = NoticeAssembler.noticeResponses(noticeResponseDtos);

        return ResponseEntity.ok(noticeResponses);
    }

    @ApiOperation(value = "공지사항 상세 보기", notes = "상세한 공지사항 데이터 응답")
    @ApiResponse(code = 200, message = "공지사항 상세 정보 반환")
    @GetMapping("/notice/{notIdx}")
    public ResponseEntity<NoticeResponse> getNoticeDetail(
            @ApiParam(value = "공지사항 번호", example = "1")
            @PathVariable Long notIdx
    ) {

        NoticeDetailRequestDto dto = NoticeAssembler.noticeDetailDto(notIdx);

        NoticeResponse noticeResponse = NoticeAssembler.noticeResponse(noticeService.getNoticeDetail(dto));

        return ResponseEntity.ok(noticeResponse);
    }

    @ApiOperation(value = "공지사항 수정")
    @ApiResponse(code = 200, message = "수정 후 공지사항 상세 정보 반환")
    @PatchMapping("/notice/modify")
    @PreAuthorize("authentication.name.equals(request.authorEmail) and hasRole('ROLE_MANAGER')")
    public ResponseEntity<NoticeResponse> modifyNotice(
            @ApiParam(value = "공지사항 수정 요청 body 정보")
            @RequestBody @Valid NoticeUpdateRequest request
    ) {

        NoticeUpdateRequestDto dto = NoticeAssembler.noticeUpdateRequestDto(request);

        Long notIdx = noticeService.modifyNotice(dto).getNotIdx();

        String redirectUrl = String.format(REDIRECT_URL, notIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "공지사항 비활성화")
    @ApiResponse(code = 200, message = "비활성화 성공")
    @PatchMapping("/notice/disable/{notIdx}")
    public ResponseEntity<Void> disableNotice(
            @ApiParam(value = "공지사항 번호", example = "1") @PathVariable Long notIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.disableNotice(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "공지사항 활성화")
    @ApiResponse(code = 200, message = "활성화 성공")
    @PatchMapping("/notice/enable/{notIdx}")
    public ResponseEntity<Void> enableNotice(
            @ApiParam(value = "공지사항 번호", example = "1") @PathVariable Long notIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.enableNotice(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 번호를 받아 공지사항 삭제 수행")
    @ApiResponse(code = 200, message = "삭제 성공")
    @DeleteMapping("/notice/{notIdx}")
    public ResponseEntity<Void> removeNotice(
            @ApiParam(value = "공지사항 번호", example = "1") @PathVariable Long notIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.removeNotice(dto);

        return ResponseEntity.noContent().build();
    }

}
