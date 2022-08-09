package org.deco.gachicoding.post.notice.domain.vo.contents;

import javax.persistence.Embeddable;

@Embeddable
public class NoticeTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    private String notTitle;

    protected NoticeTitle() {}

    public NoticeTitle(String notTitle) {
        validateMaximumLength(notTitle);
        this.notTitle = notTitle;
    }

    private void validateMaximumLength(String notContent) {
//        if (notContent.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new NoticeFormatException();
    }
}
