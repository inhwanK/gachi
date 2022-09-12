package org.deco.gachicoding.post.board.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
public class BoardSaveRequest {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    @ApiModelProperty(value = "게시판 제목", required = true, example = "오늘 가입했습니다. 잘 부탁 드립니다.")
    private String boardTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    @ApiModelProperty(value = "게시판 내용", required = true, example = "안녕하세요 반갑습니다.")
    private String boardContent;

//    @NotNull
//    @ApiModelProperty(value = "게시판 내용", required = true, example = "안녕하세요 반갑습니다.")
//    private String boardContents;
//
//    @NotNull
//    @ApiModelProperty(value = "게시판 카테고리", required = false, example = "개발 일상 토론")
//    private String boardCategory;

    @Nullable
    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
    private List<String> tags;

    private BoardSaveRequest() {}

    public BoardSaveRequest(
            String userEmail,
            String boardTitle,
            String boardContent
    ) {
        this.userEmail = userEmail;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
