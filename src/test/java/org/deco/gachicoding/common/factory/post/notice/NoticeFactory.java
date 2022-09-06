package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.user.domain.User;
import org.springframework.data.domain.Pageable;

public class NoticeFactory {
    private NoticeFactory() {}

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

    public static NoticeSaveRequest mockNoticeSaveRequest(String userEmail, String notTitle, String notContents) {
        return NoticeSaveRequest.builder()
                .userEmail(userEmail)
                .notTitle(notTitle)
                .notContent(notContents)
                .notPin(false)
                .build();
    }
}
