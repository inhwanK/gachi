package org.deco.gachicoding.post.board.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class BoardDetailRequestDto {

    @NotNull(message = "F0001")
    private Long boardIdx;

    @Builder
    public BoardDetailRequestDto(Long boardIdx) {
        this.boardIdx = boardIdx;
    }
}
