package org.deco.gachicoding.post.question.domain.vo;

import org.deco.gachicoding.exception.post.question.QuestionTitleEmptyException;
import org.deco.gachicoding.exception.post.question.QuestionTitleOverMaximumLengthException;
import org.deco.gachicoding.exception.post.question.QuestionTitleNullException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column(name = "qs_title", columnDefinition = "varchar(255)", nullable = false)
    private String queTitle;

    protected QuestionTitle() {}

    public QuestionTitle(String queTitle) {
        validateNullTitle(queTitle);
        validateMaximumLength(queTitle);
        this.queTitle = queTitle;
    }

    public String getQuestionTitle() {
        return queTitle;
    }

    public QuestionTitle update(String updateTitle) {
        if (queTitle.equals(updateTitle))
            return this;
        return new QuestionTitle(updateTitle);
    }

    private void validateNullTitle(String queTitle) {
        if (queTitle == null || queTitle.isBlank())
            throw new QuestionTitleNullException();
    }

    private void validateMaximumLength(String queTitle) {
        if (queTitle.length() > MAXIMUM_CONTENT_LENGTH)
            throw new QuestionTitleOverMaximumLengthException();
    }
}
