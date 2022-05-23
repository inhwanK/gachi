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
    private String boardTitle;
    private String boardContent;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardPin;
    private Boolean boardActivated;

    private List<FileResponseDto> files;

    private List<TagResponseDto> tags;

    @Builder
    public BoardResponseDto(Board board) {
        this.boardIdx = board.getBoardIdx();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
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