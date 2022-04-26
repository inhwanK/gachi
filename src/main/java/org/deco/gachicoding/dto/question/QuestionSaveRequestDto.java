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
    private String qsTitle;

    @NotNull
    private String qsContent;

    @Nullable
    private String qsError;

    @Nullable
    private String qsCategory;

    @Builder
    public QuestionSaveRequestDto(Long userIdx, String qsTitle, String qsContent, String qsError, String qsCategory) {
        this.userIdx = userIdx;
        this.qsTitle = qsTitle;
        this.qsContent = qsContent;
        this.qsError = qsError;
        this.qsCategory = qsCategory;
    }

    public Question toEntity(){
        return Question.builder()
                .qsTitle(qsTitle)
                .qsContent(qsContent)
                .qsError(qsError)
                .qsCategory(qsCategory)
                .build();
    }
}
