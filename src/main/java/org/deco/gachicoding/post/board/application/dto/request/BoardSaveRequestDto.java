package org.deco.gachicoding.post.board.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardSaveRequestDto {

    private String userEmail;

    private String boardTitle;

    private String boardContents;

    private String boardCategory;

    private List<String> tags;

    @Builder
    public BoardSaveRequestDto(String userEmail, String boardTitle, String boardContents, String boardCategory, List<String> tags) {
        this.userEmail = userEmail;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.tags = tags;
    }
}