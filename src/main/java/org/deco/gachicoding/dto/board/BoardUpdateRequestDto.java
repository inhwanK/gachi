package org.deco.gachicoding.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {

    private Long boardIdx;
    private String boardTitle;
    private String boardContent;

    @Builder
    public BoardUpdateRequestDto(Long boardIdx, String boardTitle, String boardContent) {
        this.boardIdx = boardIdx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
