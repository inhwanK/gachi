package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.notice.NoticeDetailResponseDto;
import org.deco.gachicoding.dto.notice.NoticeListResponseDto;
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

    @GetMapping("/swagger/test")
    @ApiOperation(value = "스웨거 테스트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Exception")
    })
    public String test() {
        return "swagger test";
    }

    @GetMapping("/notice/detail/{idx}")
    public NoticeDetailResponseDto findNoticeDetailById(@PathVariable Long idx) {
        return noticeService.findNoticeDetailById(idx);
    }

    @GetMapping("/notice/list/{page}")
    public Page<NoticeListResponseDto> findNoticeByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PathVariable int page) {
        return noticeService.findNoticeByKeyword(keyword, page);
    }

    @PostMapping("/notice")
    public Long registerNotice(@RequestBody NoticeSaveRequestDto dto) {
        return noticeService.registerNotice(dto);
    }

    @PutMapping("/notice/update/{idx}")
    public NoticeDetailResponseDto updateNotice(@PathVariable Long idx, @RequestBody NoticeUpdateRequestDto dto){
        return noticeService.updateNoticeById(idx, dto);
    }

    @PutMapping("/notice/delete/{idx}")
    public Long deleteUser(@PathVariable Long idx){
        return noticeService.deleteNoticeById(idx);
    }

}
