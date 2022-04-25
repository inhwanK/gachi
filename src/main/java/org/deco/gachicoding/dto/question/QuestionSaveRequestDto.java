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
    private String q_title;

    @NotNull
    private String q_content;

    @Nullable
    private String q_error;

    @Nullable
    private String q_category;

    @Builder
    public QuestionSaveRequestDto(Long userIdx, String qTitle, String qContent, String qError, String qCategory) {
        this.userIdx = userIdx;
        this.q_title = qTitle;
        this.q_content = qContent;
        this.q_error = qError;
        this.q_category = qCategory;
    }

    public Question toEntity(){
        return Question.builder()
                .qTitle(q_title)
                .qContent(q_content)
                .qError(q_error)
                .qCategory(q_category)
                .build();
    }
}
