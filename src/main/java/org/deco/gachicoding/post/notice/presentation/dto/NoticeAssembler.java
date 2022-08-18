package org.deco.gachicoding.post.notice.presentation.dto;

import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
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

    public static NoticeDetailDto noticeDetailDto(Long notIdx) {
        return NoticeDetailDto.builder()
                .notIdx(notIdx)
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

    public static NoticeUpdateRequestDto noticeUpdateRequestDto(Long notIdx, NoticeUpdateRequest request) {
        return NoticeUpdateRequestDto.builder()
                .userEmail(request.getUserEmail())
                .notIdx(notIdx)
                .notTitle(request.getNotTitle())
                .notContents(request.getNotContents())
                .build();
    }

    public static NoticeBasicRequestDto noticeBasicRequestDto(Long notIdx, String userEmail) {
        return NoticeBasicRequestDto.builder()
                .notIdx(notIdx)
                .userEmail(userEmail)
                .build();
    }

    public static NoticeResponse noticeResponse(NoticeResponseDto dto) {
        return NoticeResponse.builder()
                .notIdx(dto.getNotIdx())
                .userEmail(dto.getUserEmail())
                .userNick(dto.getUserNick())
                .notTitle(dto.getNotTitle())
                .notContents(dto.getNotContent())
                .notViews(dto.getNotViews())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    private static Function<NoticeResponseDto, NoticeResponse> noticeResponse() {
        return noticeResponseDto -> NoticeResponse.builder()
                .notIdx(noticeResponseDto.getNotIdx())
                .userEmail(noticeResponseDto.getUserEmail())
                .userNick(noticeResponseDto.getUserNick())
                .notTitle(noticeResponseDto.getNotTitle())
                .notContents(noticeResponseDto.getNotContent())
                .notViews(noticeResponseDto.getNotViews())
                .createdAt(noticeResponseDto.getCreatedAt())
                .updatedAt(noticeResponseDto.getUpdatedAt())
                .build();
    }
}
