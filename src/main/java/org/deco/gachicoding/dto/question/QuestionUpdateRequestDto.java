package org.deco.gachicoding.dto.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionUpdateRequestDto {

    private String qsTitle;
    private String qsContent;
    private String qsError;
    private String qsCategory;

    public QuestionUpdateRequestDto(String qsTitle, String qsContent, String qsError, String qsCategory) {
        this.qsTitle = qsTitle;
        this.qsContent = qsContent;
        this.qsError = qsError;
        this.qsCategory = qsCategory;
    }
}
