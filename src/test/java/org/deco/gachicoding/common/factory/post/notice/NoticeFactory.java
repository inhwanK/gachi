package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.application.dto.request.NoticeListRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.domain.Notice;
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

    public static Notice mockNotice(Long notIdx, User author, String notTitle, String notContents) {
        return MockNotice.builder()
                .notIdx(notIdx)
                .author(author)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();
    }

    public static NoticeSaveRequestDto mockUserNoticeSaveRequestDto() {
        return NoticeSaveRequestDto.builder()
                .userEmail("test@gachicoding.com")
                .notTitle("테스트 공지 제목")
                .notContents("테스트 공지 내용")
                .notPin(false)
                .build();
    }

    public static NoticeSaveRequestDto mockGuestNoticeSaveRequestDto() {
        return NoticeSaveRequestDto.builder()
                .notTitle("테스트 공지 제목")
                .notContents("테스트 공지 내용")
                .notPin(false)
                .build();
    }

    public static NoticeListRequestDto mockEmptyKeywordNoticeListRequestDto(Pageable pageable) {
        return NoticeListRequestDto.builder()
                .pageable(pageable)
                .build();
    }

    public static NoticeListRequestDto mockEmptyKeywordNoticeListRequestDto(String keyword, Pageable pageable) {
        return NoticeListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }
}
