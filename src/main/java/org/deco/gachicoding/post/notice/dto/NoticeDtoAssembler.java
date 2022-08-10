package org.deco.gachicoding.post.notice.dto;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.dto.response.NoticeResponseDto;
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
                .map(notice -> convertForm(notice))
                .collect(toList());
    }

    private static NoticeResponseDto convertForm(Notice notice) {
        return NoticeResponseDto.builder()
                .notIdx(notice.getNotIdx())
                .userEmail(notice.getWriterEmail())
                .userNick(notice.getWriterNick())
                .notTitle(notice.getNotTitle())
                .notContents(notice.getNotContents())
                .notViews(notice.getNotViews())
                .notRegdate(notice.getNotRegdate())
                .build();
    }
}
