package org.deco.gachicoding.post.notice.domain.vo.contents;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class NoticeContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 10000;

    @Lob
    private String notContents;

    protected NoticeContents() {}

    public NoticeContents(String notContents) {
        validateMaximumLength(notContents);
        this.notContents = notContents;
    }

    private void validateMaximumLength(String notContents) {
        // 개발
//        if (notContent.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new NoticeFormatException();
    }
}
