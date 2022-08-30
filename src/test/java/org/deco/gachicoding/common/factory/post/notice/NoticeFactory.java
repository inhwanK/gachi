package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.user.domain.User;

public class NoticeFactory {
    private NoticeFactory() {}

    public static Notice notice(User author) {
        return MockNotice.builder()
                .author(author)
                .build();
    }
}
