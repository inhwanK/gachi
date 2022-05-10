package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.service.notice.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 등록")
    @PostMapping("/notice")
    public Long registerNotice(@RequestBody NoticeSaveRequestDto dto) {
        return noticeService.registerNotice(dto);
    }

    @ApiOperation(value = "공지사항 리스트 보기")
    @GetMapping("/notice/list")
    public Page<NoticeResponseDto> getNoticeList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return noticeService.getNoticeList(keyword, pageable);
    }

    @ApiOperation(value = "공지사항 상세 보기")
    @GetMapping("/notice/{notIdx}")
    public NoticeResponseDto getNoticeDetail(@PathVariable Long notIdx) {
        return noticeService.getNoticeDetail(notIdx);
    }

    @ApiOperation(value = "공지사항 수정")
    @PutMapping("/notice/modify")
    public NoticeResponseDto modifyNotice(@RequestBody NoticeUpdateRequestDto dto){
        return noticeService.modifyNotice(dto);
    }

    @ApiOperation(value = "공지사항 비활성화")
    @PutMapping("/notice/disable/{notIdx}")
    public void disableNotice(@PathVariable Long notIdx){
        noticeService.disableNotice(notIdx);
    }

    @ApiOperation(value = "공지사항 활성화")
    @PutMapping("/notice/enable/{notIdx}")
    public void enableNotice(@PathVariable Long notIdx){
        noticeService.enableNotice(notIdx);
    }

    @ApiOperation(value = "공지사항 삭제")
    @DeleteMapping("/notice/remove/{notIdx}")
    public void removeNotice(@PathVariable Long notIdx){
        noticeService.removeNotice(notIdx);
    }

}
