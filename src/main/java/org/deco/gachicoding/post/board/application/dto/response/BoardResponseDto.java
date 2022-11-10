package org.deco.gachicoding.post.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.post.PostResponseDto;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto implements PostResponseDto {

    private Long boardIdx;
    private User author;

    private String boardTitle;
    private String boardContents;
    private String boardCategory;
    private Long boardViews;
    private Boolean boardLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<TagResponseDto> tags;

    @Builder
    public BoardResponseDto(
            Long boardIdx,
            User author,
            String boardTitle,
            String boardContents,
            String boardCategory,
            Long boardViews,
            Boolean boardLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.boardIdx = boardIdx;

        this.author = author;

        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.boardLocked = boardLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
//        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
    }
}