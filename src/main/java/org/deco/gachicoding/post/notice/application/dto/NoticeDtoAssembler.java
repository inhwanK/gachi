package org.deco.gachicoding.post.notice.application.dto;

import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.user.domain.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class NoticeDtoAssembler {

    private NoticeDtoAssembler() {}

    public static Notice notice(User user, NoticeSaveRequestDto dto) {
        return Notice.builder()
                .author(user)
                .notTitle(dto.getNotTitle())
                .notContents(dto.getNotContents())
                .notPin(dto.getNotPin())
                .build();
    }

    public static List<NoticeResponseDto> noticeResponseDtos(List<Notice> notices) {
        return notices.stream()
                .map(NoticeDtoAssembler::convertForm)
                .collect(toList());
    }

    public static NoticeResponseDto noticeResponseDto(Notice notice) {
        return convertForm(notice);
    }

    private static NoticeResponseDto convertForm(Notice notice) {
        return NoticeResponseDto.builder()
                .notIdx(notice.getNotIdx())
                .author(notice.getAuthor())
                .notTitle(notice.getNotTitle())
                .notContents(notice.getNotContents())
                .notViews(notice.getNotViews())
                .notPin(notice.getPin())
                .notEnabled(notice.getNotEnabled())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
