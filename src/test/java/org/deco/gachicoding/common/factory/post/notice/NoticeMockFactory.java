package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.deco.gachicoding.user.domain.User;
import org.springframework.data.domain.Pageable;

public class NoticeMockFactory {

    private static final Long DEFAULT_NOTICE_IDX = 1L;
    private static final String DEFAULT_NOTICE_TITLE = "Test Notice Title";
    private static final String DEFAULT_NOTICE_CONTENTS = "Test Notice Contents";
    private static final Long DEFAULT_NOTICE_VIEWS = 0L;
    private static final Boolean DEFAULT_NOTICE_ENABLED = Boolean.TRUE;

    private static final String UPDATE_NOTICE_TITLE = "Update Test Notice Title";
    private static final String UPDATE_NOTICE_CONTENTS = "Update Test Notice Contents";

    private NoticeMockFactory() {}

    /* Notice Object start */
    public static Notice createDefaultStateNoticeMock(User author) {
        return NoticeMock.builder()
                .author(author)
                .notIdx(DEFAULT_NOTICE_IDX)
                .notTitle(DEFAULT_NOTICE_TITLE)
                .notContents(DEFAULT_NOTICE_CONTENTS)
                .notViews(DEFAULT_NOTICE_VIEWS)
                .notEnable(DEFAULT_NOTICE_ENABLED)
                .build();
    }

    public static Notice createNotice(User author) {
        return NoticeMock.builder()
                .author(author)
                .build();
    }

    public static Notice createNotice(
            Long notIdx,
            User author
    ) {
        return NoticeMock.builder()
                .notIdx(notIdx)
                .author(author)
                .build();
    }

    public static Notice createNotice(
            Long notIdx,
            User author,
            Boolean notEnabled
    ) {
        return NoticeMock.builder()
                .notIdx(notIdx)
                .author(author)
                .notEnable(notEnabled)
                .build();
    }

    public static Notice createNotice(
            User author,
            Boolean notEnabled
    ) {
        return NoticeMock.builder()
                .author(author)
                .notEnable(notEnabled)
                .build();
    }

    public static Notice createNotice(
            Long notIdx,
            User author,
            String notTitle,
            String notContents,
            Boolean notEnabled
    ) {
        return NoticeMock.builder()
                .notIdx(notIdx)
                .author(author)
                .notTitle(notTitle)
                .notContents(notContents)
                .notEnable(notEnabled)
                .build();
    }

    public static Notice createNotice() {
        return NoticeMock.builder()
                .notIdx(DEFAULT_NOTICE_IDX)
//                .author(author)
                .notTitle(DEFAULT_NOTICE_TITLE)
                .notContents(DEFAULT_NOTICE_CONTENTS)
                .notViews(DEFAULT_NOTICE_VIEWS)
                .notEnable(DEFAULT_NOTICE_ENABLED)
                .build();
    }
    /* Notice Object end */

    /* Notice Dto start */
    public static NoticeSaveRequestDto mockNoticeSaveRequestDto(
            String userEmail,
            String notTitle,
            String notContents
    ) {
        return NoticeSaveRequestDto.builder()
                .userEmail(userEmail)
                .notTitle(notTitle)
                .notContents(notContents)
                .notPin(false)
                .build();
    }

    public static NoticeListRequestDto mockNoticeListRequestDto(
            String keyword,
            Pageable pageable
    ) {
        return NoticeListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }

    public static NoticeDetailRequestDto mockNoticeDetailRequestDto(
            Long notIdx
    ) {
        return NoticeDetailRequestDto.builder()
                .notIdx(notIdx)
                .build();
    }

    public static NoticeUpdateRequestDto mockNoticeUpdateRequestDto(
            String userEmail,
            Long notIdx,
            String notTitle,
            String notContents
    ) {
        return NoticeUpdateRequestDto.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();
    }

    public static NoticeBasicRequestDto mockNoticeBasicRequestDto(
            String userEmail,
            Long notIdx
    ) {
        return NoticeBasicRequestDto.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .build();
    }

    public static NoticeResponseDto mockNoticeResponseDto(
            Notice notice
    ) {
        return NoticeResponseDto.builder()
                .notIdx(notice.getNotIdx())
                .author(notice.getAuthor())
                .notTitle(notice.getNotTitle())
                .notContents(notice.getNotContents())
                .notViews(notice.getNotViews())
                .notPin(notice.getPin())
                .notEnabled(notice.getEnabled())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
    /* Notice Dto end */

    /* Notice Request start */
    public static NoticeSaveRequest mockNoticeSaveRequest(
            String userEmail,
            String notTitle,
            String notContents
    ) {
        return NoticeSaveRequest.builder()
                .userEmail(userEmail)
                .notTitle(notTitle)
                .notContent(notContents)
                .notPin(false)
                .build();
    }

    public static NoticeUpdateRequest mockNoticeUpdateRequest(
            String userEmail,
            Long notIdx,
            String notTitle,
            String notContents
    ) {
        return NoticeUpdateRequest.builder()
                .userEmail(userEmail)
                .notIdx(notIdx)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();
    }

    public static NoticeDetailRequestDto mockNoticeDetailRequest(
            Long notIdx
    ) {
        return NoticeDetailRequestDto.builder()
                .notIdx(notIdx)
                .build();
    }

    public static NoticeResponse mockNoticeResponse(
            NoticeResponseDto dto
    ) {
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
