package org.deco.gachicoding.post.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.PostResponseDto;
import org.deco.gachicoding.file.dto.response.FileResponseDto;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardPostResponseDto implements PostResponseDto {

    private Long boardIdx;
    private String userEmail;
    private String userNick;

    private String boardTitle;
    private String boardContent;
//    private String boardCategory;
    private Long boardViews;
    private LocalDateTime boardRegdate;
//    private Boolean boardActivated;

//    private List<FileResponseDto> files;
    private List<TagResponseDto> tags;

    @Builder
    public BoardPostResponseDto(Board board) {
        this.boardIdx = board.getBoardIdx();
        this.userEmail = board.getWriter().getUserEmail();
        this.userNick = board.getWriter().getUserNick();

        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContents();
//        this.boardCategory = board.getBoardCategory();
        this.boardViews = board.getBoardViews();
        this.boardRegdate = board.getBoardRegdate();
//        this.boardActivated = board.getBoardActivated();
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
//        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}