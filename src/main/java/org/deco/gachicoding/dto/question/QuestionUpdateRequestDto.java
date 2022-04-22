package org.deco.gachicoding.dto.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionUpdateRequestDto {

    private String qTitle;
    private String qContent;
    private String qError;
    private String qCategory;

    public QuestionUpdateRequestDto(String qTitle, String qContent, String qError, String qCategory) {
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qError = qError;
        this.qCategory = qCategory;
    }
}
