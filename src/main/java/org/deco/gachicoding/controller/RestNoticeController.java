package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.FileService;
import org.deco.gachicoding.service.NoticeService;
import org.deco.gachicoding.service.TagService;
import org.deco.gachicoding.service.impl.S3ServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {
    private final NoticeService noticeService;
    private final S3ServiceImpl s3Service;
    private final TagService tagService;
    private final FileService fileService;
    private final String type = "notice";

    @ApiOperation(value = "공지사항 등록")
    @PostMapping("/notice")
    public Long registerNotice(@RequestBody NoticeSaveRequestDto dto) {
        log.info("{} Register Controller", "Notice");
        Long noticeIdx = noticeService.registerNotice(dto);

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
            tagService.registerBoardTag(noticeIdx, dto.getTags(), type);
        } catch (NullPointerException e) {
            log.error("{} Board Tags Error", "Notice");
            e.printStackTrace();
        }
        return noticeIdx;
    }

    @ApiOperation(value = "공지사항 리스트 보기")
    @GetMapping("/notice/list")
    public Page<NoticeResponseDto> getNoticeList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return noticeService.getNoticeList(keyword, pageable);
    }

    @ApiOperation(value = "공지사항 상세 보기")
    @GetMapping("/notice/{notIdx}")
    public NoticeResponseDto getNoticeDetail(@PathVariable Long notIdx) {
        NoticeResponseDto result = noticeService.getNoticeDetail(notIdx);
        fileService.getFiles(notIdx, type, result);
        tagService.getTags(notIdx, type, result);
        return result;
    }

    @ApiOperation(value = "공지사항 수정")
    @PutMapping("/notice/modify")
    public NoticeResponseDto modifyNotice(@RequestBody NoticeUpdateRequestDto dto){
        return noticeService.modifyNotice(dto);
    }

    @ApiOperation(value = "공지사항 비활성화")
    @PutMapping("/notice/disable/{notIdx}")
    public ResponseEntity<ResponseState> disableNotice(@PathVariable Long notIdx){
        return noticeService.disableNotice(notIdx);
    }

    @ApiOperation(value = "공지사항 활성화")
    @PutMapping("/notice/enable/{notIdx}")
    public ResponseEntity<ResponseState> enableNotice(@PathVariable Long notIdx){
        return noticeService.enableNotice(notIdx);
    }

    @ApiOperation(value = "공지사항 삭제")
    @DeleteMapping("/notice/remove/{notIdx}")
    public ResponseEntity<ResponseState> removeNotice(@PathVariable Long notIdx){
        return noticeService.removeNotice(notIdx);
    }

}
