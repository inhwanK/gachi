package org.deco.gachicoding.dto.question;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.question.Question;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {
    @NotNull
    private Long userIdx;

    @NotNull
    private String qTitle;

    @NotNull
    private String qContent;

    @Nullable
    private String qError;

    @Nullable
    private String qCategory;

    @Builder
    public QuestionSaveRequestDto(Long userIdx, String qTitle, String qContent, String qError, String qCategory) {
        this.userIdx = userIdx;
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qError = qError;
        this.qCategory = qCategory;
    }

    public Question toEntity(){
        return Question.builder()
                .qTitle(qTitle)
                .qContent(qContent)
                .qError(qError)
                .qCategory(qCategory)
                .build();
    }
}
