package org.deco.gachicoding.post.answer.domain.vo;

import org.deco.gachicoding.exception.post.answer.AnswerContentsEmptyException;
import org.deco.gachicoding.exception.post.answer.AnswerContentsNullException;
import org.deco.gachicoding.exception.post.answer.AnswerContentsOverMaximumLengthException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AnswerContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 100000;

    @Column(name = "as_contents", columnDefinition = "text", nullable = false)
    private String ansContents;

    protected AnswerContents() {}

    public AnswerContents(String queContents) {
        validateNullContents(queContents);
        validateEmptyContents(queContents);
        validateMaximumLength(queContents);
        this.ansContents = queContents;
    }

    public String getAnswerContents() {
        return ansContents;
    }

    public AnswerContents update(String updateContents) {
        if (ansContents.equals(updateContents))
            return this;
        return new AnswerContents(updateContents);
    }

    private void validateNullContents(String ansContents) {
        if (ansContents == null)
            throw new AnswerContentsNullException();
    }

    private void validateEmptyContents(String ansContents) {
        if (ansContents.isEmpty())
            throw new AnswerContentsEmptyException();
    }

    private void validateMaximumLength(String ansContents) {
        // 개발
        if (ansContents.length() > MAXIMUM_CONTENT_LENGTH)
            throw new AnswerContentsOverMaximumLengthException();
    }
}
