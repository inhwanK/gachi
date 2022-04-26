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
    private Long qsIdx;

    @NotNull
    private String asContent;

    @Builder
    public AnswerSaveRequestDto(Long userIdx, Long qsIdx, String asContent) {
        this.userIdx = userIdx;
        this.qsIdx = qsIdx;
        this.asContent = asContent;
    }

    public Answer toEntity(){
        return Answer.builder()
                .asContent(asContent)
                .build();
    }
}
