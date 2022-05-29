package org.deco.gachicoding.dto.answer;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.answer.Answer;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSaveRequestDto {

    @ApiModelProperty(value = "작성자 번호", required = true, example = "1")
    @NotNull
    private Long userIdx;

    @ApiModelProperty(value = "답변할 질문 글 번호", required = true, example = "1")
    @NotNull
    private Long queIdx;

    @ApiModelProperty(value = "답변 내용", required = true, example = "그거는 해결이 안되요~")
    @NotNull
    private String ansContent;

    @Builder
    public AnswerSaveRequestDto(Long userIdx, Long queIdx, String ansContent) {
        this.userIdx = userIdx;
        this.queIdx = queIdx;
        this.ansContent = ansContent;
    }

    public Answer toEntity(){
        return Answer.builder()
                .ansContent(ansContent)
                .build();
    }
}
