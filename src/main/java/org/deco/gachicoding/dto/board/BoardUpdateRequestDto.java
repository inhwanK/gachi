package org.deco.gachicoding.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {

    @NotNull
    @ApiModelProperty(value = "작성자 번호", required = true, example = "1")
    private Long userIdx;

    @NotNull
    @ApiModelProperty(value = "게시판 번호", required = true, example = "1")
    private Long boardIdx;

    @NotNull
    @ApiModelProperty(value = "게시판 제목", required = false, example = "수정된 제목")
    private String boardTitle;

    @NotNull
    @ApiModelProperty(value = "게시판 내용", required = false, example = "수정된 내용")
    private String boardContent;

    @Builder
    public BoardUpdateRequestDto(Long userIdx, Long boardIdx, String boardTitle, String boardContent) {
        this.userIdx = userIdx;
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
