package org.deco.gachicoding.post.board.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse {

    private Long boardIdx;

    private String authorEmail;
    private String authorNick;

    private String boardTitle;
    private String boardContents;
    private String boardCategory;
    private Long boardViews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<TagResponseDto> tags;

    @Builder
    public BoardResponse(Long boardIdx, String authorEmail, String authorNick, String boardTitle, String boardContents, String boardCategory, Long boardViews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardIdx = boardIdx;

        this.authorEmail = authorEmail;
        this.authorNick = authorNick;

        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
