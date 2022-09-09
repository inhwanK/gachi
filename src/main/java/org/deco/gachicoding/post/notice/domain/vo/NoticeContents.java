package org.deco.gachicoding.post.notice.domain.vo;

import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.exception.post.notice.NoticeContentsEmptyException;
import org.deco.gachicoding.exception.post.notice.NoticeContentsFormatException;
import org.deco.gachicoding.exception.post.notice.NoticeContentsNullException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Embeddable
public class NoticeContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 10000;

    @Column(name = "not_contents", columnDefinition = "text", nullable = false)
    private String notContents;

    protected NoticeContents() {}

    public NoticeContents(String notContents) {
        validateNullContents(notContents);
        validateEmptyContents(notContents);
        validateMaximumLength(notContents);
        this.notContents = notContents;
    }

    public String getNoticeContents() {
        return notContents;
    }

    private void validateNullContents(String notContents) {
        if (notContents == null)
            throw new NoticeContentsNullException();
    }

    private void validateEmptyContents(String notContents) {
        if (notContents.isEmpty())
            throw new NoticeContentsEmptyException();
    }

    private void validateMaximumLength(String notContents) {
        // 개발
        if (notContents.length() > MAXIMUM_CONTENT_LENGTH)
            throw new NoticeContentsFormatException();
    }
}
