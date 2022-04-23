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
    private Long qIdx;

    @NotNull
    private String aContent;

    @Builder
    public AnswerSaveRequestDto(Long userIdx, Long qIdx, String aContent) {
        this.userIdx = userIdx;
        this.qIdx = qIdx;
        this.aContent = aContent;
    }

    public Answer toEntity(){
        return Answer.builder()
                .aContent(aContent)
                .build();
    }
}
