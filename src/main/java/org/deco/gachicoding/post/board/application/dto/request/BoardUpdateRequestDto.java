package org.deco.gachicoding.post.board.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class BoardUpdateRequestDto {

    private String userEmail;

    private Long boardIdx;

    private String boardTitle;

    private String boardContents;

    @Builder
    public BoardUpdateRequestDto(String userEmail, Long boardIdx, String boardTitle, String boardContents) {
        this.userEmail = userEmail;
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
    }
}
