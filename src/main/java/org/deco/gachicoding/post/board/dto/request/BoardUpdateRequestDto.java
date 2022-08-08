package org.deco.gachicoding.post.board.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {

    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

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
    public BoardUpdateRequestDto(String userEmail, Long boardIdx, String boardTitle, String boardContent) {
        this.userEmail = userEmail;
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
