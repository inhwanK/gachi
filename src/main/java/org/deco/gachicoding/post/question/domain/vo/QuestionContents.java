package org.deco.gachicoding.post.question.domain.vo;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.exception.post.question.QuestionContentsEmptyException;
import org.deco.gachicoding.exception.post.question.QuestionContentsOverMaximumLengthException;
import org.deco.gachicoding.exception.post.question.QuestionContentsNullException;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Getter
@Embeddable
public class QuestionContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 100000;

    @Column(name = "qs_general_content", columnDefinition = "text", nullable = false)
    private String queGeneralContent;
    @Column(name = "qs_code_content", columnDefinition = "text")
    private String queCodeContent;
    @Column(name = "qs_error_content", columnDefinition = "text")
    private String queErrorContent;

    protected QuestionContents() {
    }

    @Builder
    public QuestionContents(String queGeneralContent, String queCodeContent, String queErrorContent) {
        validateEssentialContents(queGeneralContent);
        validateMaximumLength(queCodeContent);
        validateMaximumLength(queErrorContent);
        this.queGeneralContent = queGeneralContent;
        this.queCodeContent = queCodeContent;
        this.queErrorContent = queErrorContent;
    }

    // 수정 중
    public QuestionContents getQuestionContents() {
        return this;
    }

    public QuestionContents update(QuestionContents updateContents) {
        return QuestionContents.builder()
                .queGeneralContent(updateContents.getQueGeneralContent())
                .queCodeContent(updateContents.getQueCodeContent())
                .queErrorContent(updateContents.getQueErrorContent())
                .build();
    }

    private void validateEssentialContents(String contents) {
        validateNullOrBlankContents(contents);
        validateMaximumLength(contents);
    }


    private void validateNullOrBlankContents(String contents) {
        if (contents == null || contents.isBlank())
            throw new QuestionContentsNullException();
    }

    private void validateMaximumLength(String contents) {
        // 개발
        if (contents != null && contents.length() > MAXIMUM_CONTENT_LENGTH)
            throw new QuestionContentsOverMaximumLengthException();
    }
}
