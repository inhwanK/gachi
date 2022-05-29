package org.deco.gachicoding.dto.board;

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
    private String userEmail;

    @NotNull
    private String boardTitle;

    @NotNull
    private String boardContent;

    @Nullable
    private Long boardViews;

    @Nullable
    private LocalDateTime boardRegdate;

    @Nullable
    private Boolean boardPin;

    @Nullable
    private Boolean boardActivated;

    @Nullable
    private List<String> files;

    @Nullable
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
