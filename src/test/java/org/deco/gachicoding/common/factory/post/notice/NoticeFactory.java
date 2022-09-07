package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.deco.gachicoding.user.domain.User;
import org.springframework.data.domain.Pageable;

public class NoticeFactory {
    private NoticeFactory() {}

    /* Notice Object start */
    public static Notice mockNotice(Long notIdx, User author, Boolean notLocked) {
        return MockNotice.builder()
                .notIdx(notIdx)
                .author(author)
                .notLocked(notLocked)
                .build();
    }

    public static Notice mockNotice(Long notIdx, User author, String notTitle, String notContents, Boolean notLocked) {
        return MockNotice.builder()
                .notIdx(notIdx)
                .author(author)
                .notTitle(notTitle)
                .notContents(notContents)
                .notLocked(notLocked)
                .build();
    }

    public static NoticeSaveRequestDto mockNoticeSaveRequestDto(String userEmail, String notTitle, String notContents) {
        return NoticeSaveRequestDto.builder()
                .userEmail(userEmail)
                .notTitle(notTitle)
                .notContents(notContents)
                .notPin(false)
                .build();
    }
    /* Notice Object end */

    /* Notice Dto start */
    public static NoticeListRequestDto mockNoticeListRequestDto(String keyword, Pageable pageable) {
        return NoticeListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }

    public static NoticeDetailRequestDto mockNoticeDetailRequestDto(Long notIdx) {
        return NoticeDetailRequestDto.builder()
                .notIdx(notIdx)
                .build();
    }

    public static NoticeUpdateRequestDto mockNoticeUpdateRequestDto(String userEmail, Long notIdx, String notTitle, String notContents) {
        return NoticeUpdateRequestDto.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();
    }

    public static NoticeBasicRequestDto mockNoticeBasicRequestDto(String userEmail, Long notIdx) {
        return NoticeBasicRequestDto.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .build();
    }

    public static NoticeResponseDto mockNoticeResponseDto(Notice notice) {
        return NoticeResponseDto.builder()
                .notIdx(notice.getNotIdx())
                .author(notice.getAuthor())
                .notTitle(notice.getNotTitle())
                .notContents(notice.getNotContents())
                .notViews(notice.getNotViews())
                .notPin(notice.getNotPin())
                .notLocked(notice.getNotLocked())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
    /* Notice Dto end */

    /* Notice Request start */
    public static NoticeSaveRequest mockNoticeSaveRequest(String userEmail, String notTitle, String notContents) {
        return NoticeSaveRequest.builder()
                .userEmail(userEmail)
                .notTitle(notTitle)
                .notContent(notContents)
                .notPin(false)
                .build();
    }

    public static NoticeUpdateRequest mockNoticeUpdateRequest(String userEmail, Long notIdx, String notTitle, String notContents) {
        return NoticeUpdateRequest.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();
    }

    public static NoticeDetailRequestDto mockNoticeDetailRequest(Long notIdx) {
        return NoticeDetailRequestDto.builder()
                .notIdx(notIdx)
                .build();
    }

    public static NoticeResponse mockNoticeResponse(NoticeResponseDto dto) {
        return NoticeResponse.builder()
                .notIdx(dto.getNotIdx())
                .authorEmail(dto.getAuthor().getUserEmail())
                .authorNick(dto.getAuthor().getUserNick())
                .notTitle(dto.getNotTitle())
                .notContents(dto.getNotContents())
                .notViews(dto.getNotViews())
                .notPin(dto.getNotPin())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
    /* Notice Request end */
}
