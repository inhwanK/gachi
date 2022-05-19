package org.deco.gachicoding.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.board.Board;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardSaveRequestDto {

    private String boardTitle;
    private String boardContent;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private boolean boardPin;
    private boolean boardActivated;

    @Nullable
    private List<String> files;

    public Board toEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardViews(boardViews)
                .boardRegdate(boardRegdate)
                .boardPin(boardPin)
                .boardActivated(boardActivated)
                .build();
    }
}
