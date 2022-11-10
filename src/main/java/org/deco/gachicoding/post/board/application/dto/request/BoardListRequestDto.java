package org.deco.gachicoding.post.board.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

@Getter
public class BoardListRequestDto {

    @NotNull(message = "F0001")
    private String keyword;

    @NotNull(message = "F0001")
    private Pageable pageable;

    @Builder
    public BoardListRequestDto(String keyword, Pageable pageable) {
        this.keyword = keyword;
        this.pageable = pageable;
    }
}
