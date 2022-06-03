package org.deco.gachicoding.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.file.FileResponseDto;
import org.deco.gachicoding.dto.tag.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto implements ResponseDto {

    private Long boardIdx;
    private String userEmail;
    private String userNick;

    private String boardTitle;
    private String boardContent;
    private String boardType;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardPin;
    private Boolean boardActivated;

    private List<FileResponseDto> files;
    private List<TagResponseDto> tags;

    @Builder
    public BoardResponseDto(Board board) {
        this.boardIdx = board.getBoardIdx();
        this.userEmail = board.getWriter().getUserEmail();
        this.userNick = board.getWriter().getUserNick();

        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardType = board.getBoardType();
        this.boardViews = board.getBoardViews();
        this.boardRegdate = board.getBoardRegdate();
        this.boardPin = board.getBoardPin();
        this.boardActivated = board.getBoardActivated();
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}