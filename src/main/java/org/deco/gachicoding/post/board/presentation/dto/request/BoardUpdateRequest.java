package org.deco.gachicoding.post.board.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
public class BoardUpdateRequest {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "게시판 번호", required = true, example = "1")
    private Long boardIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    @ApiModelProperty(value = "게시판 제목", required = false, example = "수정된 제목")
    private String boardTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    @ApiModelProperty(value = "게시판 내용", required = false, example = "수정된 내용")
    private String boardContents;

    private BoardUpdateRequest() {}

    public BoardUpdateRequest(
            String userEmail,
            Long boardIdx,
            String boardTitle,
            String boardContents
    ) {
        this.userEmail = userEmail;
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
    }
}
