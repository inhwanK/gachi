package org.deco.gachicoding.post.board.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class BoardBasicRequestDto {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    private String userEmail;

    @NotNull(message = "F0001")
    private Long boardIdx;

    @Builder
    public BoardBasicRequestDto(String userEmail, Long boardIdx) {
        this.userEmail = userEmail;
        this.boardIdx = boardIdx;
    }
}
