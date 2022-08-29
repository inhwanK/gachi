package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.user.domain.User;

public class NoticeFactory {
    private NoticeFactory() {}

    public static Notice mockNotice(Long notIdx, User author) {
        return MockNotice.builder()
                .notIdx(notIdx)
                .author(author)
                .build();
    }

    public static NoticeSaveRequestDto mockLoginNoticeSaveRequestDto() {
        return NoticeSaveRequestDto.builder()
                .userEmail("test@gachicoding.com")
                .notTitle("테스트 공지 제목")
                .notContents("테스트 공지 내용")
                .notPin(false)
                .build();
    }
}
