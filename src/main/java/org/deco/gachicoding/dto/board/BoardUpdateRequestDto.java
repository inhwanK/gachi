package org.deco.gachicoding.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {

    private String boardTitle;
    private String boardContent;

    @Builder
    public BoardUpdateRequestDto(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
