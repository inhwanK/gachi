package org.deco.gachicoding.dto.answer;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.answer.Answer;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSaveRequestDto {
    @NotNull
    private Long userIdx;

    @NotNull
    private Long q_idx;

    @NotNull
    private String a_content;

    @Builder
    public AnswerSaveRequestDto(Long userIdx, Long qIdx, String aContent) {
        this.userIdx = userIdx;
        this.q_idx = qIdx;
        this.a_content = aContent;
    }

    public Answer toEntity(){
        return Answer.builder()
                .aContent(a_content)
                .build();
    }
}
