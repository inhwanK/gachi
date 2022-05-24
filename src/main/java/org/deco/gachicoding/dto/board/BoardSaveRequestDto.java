package org.deco.gachicoding.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.user.User;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardSaveRequestDto {

    private String userEmail;
    private String boardTitle;
    private String boardContent;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardPin;
    private Boolean boardActivated;

    @Nullable
    private List<String> files;

    @Nullable
    private List<String> tags;

    public Board toEntity(User writer) {
        return Board.builder()
                .writer(writer)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardViews(boardViews)
                .boardRegdate(boardRegdate)
                .boardPin(boardPin)
                .boardActivated(boardActivated)
                .build();
    }
}
