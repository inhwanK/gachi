package org.deco.gachicoding.post.notice.presentation.dto;

import org.deco.gachicoding.post.notice.application.dto.request.NoticeListRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoticeAssembler {

    private NoticeAssembler() {}

    public static NoticeSaveRequestDto noticeSaveRequestDto(NoticeSaveRequest request) {
        return NoticeSaveRequestDto.builder()
                .userEmail(request.getUserEmail())
                .notTitle(request.getNotTitle())
                .notContents(request.getNotContents())
                .notPin(request.getNotPin())
                .tags(request.getTags())
                .build();
    }

    public static NoticeListRequestDto noticeListRequestDto(String keyword, Pageable pageable) {
        return NoticeListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }

    public static List<NoticeResponse> noticeResponses(List<NoticeResponseDto> noticeResponseDtos) {
        return noticeResponseDtos.stream()
                .map(noticeResponse())
                .collect(Collectors.toList());
    }

    private static Function<NoticeResponseDto, NoticeResponse> noticeResponse() {
        return noticeResponseDto -> NoticeResponse.builder()
                .notIdx(noticeResponseDto.getNotIdx())
                .userEmail(noticeResponseDto.getUserEmail())
                .userNick(noticeResponseDto.getUserNick())
                .notTitle(noticeResponseDto.getNotTitle())
                .notContents(noticeResponseDto.getNotContent())
                .notViews(noticeResponseDto.getNotViews())
                .notRegdate(noticeResponseDto.getNotRegdate())
                .build();
    }
}
