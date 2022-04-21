package org.deco.gachicoding.controller;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.service.notice.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice/detail/{idx}")
    public NoticeResponseDto findNoticeDetailById(@PathVariable Long idx) {
        return noticeService.findNoticeDetailById(idx);
    }

    @GetMapping("/notice/list/{page}")
    public Page<NoticeResponseDto> findNoticeByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PathVariable int page) {
        return noticeService.findNoticeByKeyword(keyword, page);
    }

    @PostMapping("/notice")
    public Long registerNotice(@RequestBody NoticeSaveRequestDto dto) {
        return noticeService.registerNotice(dto);
    }

    @PutMapping("/notice/update/{idx}")
    public NoticeResponseDto updateNotice(@PathVariable Long idx, @RequestBody NoticeUpdateRequestDto dto){
        return noticeService.updateNoticeById(idx, dto);
    }

    @PutMapping("/notice/delete/{idx}")
    public Long deleteUser(@PathVariable Long idx){
        return noticeService.deleteNoticeById(idx);
    }

}
