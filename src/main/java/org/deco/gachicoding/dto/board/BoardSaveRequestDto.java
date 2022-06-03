package org.deco.gachicoding.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.user.User;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardSaveRequestDto {

    @NotNull
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "게시판 제목", required = true, example = "오늘 가입했습니다. 잘 부탁 드립니다.")
    private String boardTitle;

    @NotNull
    @ApiModelProperty(value = "게시판 내용", required = true, example = "안녕하세요 반갑습니다.")
    private String boardContent;

    @Nullable
    @ApiModelProperty(value = "조회수", required = false, example = "0")
    private Long boardViews;

    @Nullable
    @ApiModelProperty(value = "작성일", required = false, example = "2022.05.30")
    private LocalDateTime boardRegdate;

    @Nullable
    @ApiModelProperty(value = "고정 여부", required = false, example = "false")
    private Boolean boardPin;

    @Nullable
    @ApiModelProperty(value = "게시판 상태", required = false, example = "true")
    private Boolean boardActivated;

    @Nullable
    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
    private List<String> tags;

    public Board toEntity(User writer, String boardType) {
        return Board.builder()
                .writer(writer)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardType(boardType)
                .boardViews(boardViews)
                .boardRegdate(boardRegdate)
                .boardPin(boardPin)
                .boardActivated(boardActivated)
                .build();
    }
}
