package org.deco.gachicoding.post.question.domain.vo;

import org.deco.gachicoding.exception.post.question.QuestionContentsEmptyException;
import org.deco.gachicoding.exception.post.question.QuestionContentsOverMaximumLengthException;
import org.deco.gachicoding.exception.post.question.QuestionContentsNullException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 100000;

    @Column(name = "qs_contents", columnDefinition = "text", nullable = false)
    private String queContents;

    protected QuestionContents() {}

    public QuestionContents(String queContents) {
        validateNullContents(queContents);
        validateEmptyContents(queContents);
        validateMaximumLength(queContents);
        this.queContents = queContents;
    }

    public String getQuestionContents() {
        return queContents;
    }

    public QuestionContents update(String updateContents) {
        if (queContents.equals(updateContents))
            return this;
        return new QuestionContents(updateContents);
    }

    private void validateNullContents(String queContents) {
        if (queContents == null)
            throw new QuestionContentsNullException();
    }

    private void validateEmptyContents(String queContents) {
        if (queContents.isEmpty())
            throw new QuestionContentsEmptyException();
    }

    private void validateMaximumLength(String queContents) {
        // 개발
        if (queContents.length() > MAXIMUM_CONTENT_LENGTH)
            throw new QuestionContentsOverMaximumLengthException();
    }
}
