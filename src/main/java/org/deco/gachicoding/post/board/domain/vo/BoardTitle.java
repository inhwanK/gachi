package org.deco.gachicoding.post.board.domain.vo;

import org.deco.gachicoding.exception.post.board.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoardTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column(name = "board_title", columnDefinition = "varchar(255)", nullable = false)
    private String boardTitle;

    protected BoardTitle() {}

    public BoardTitle(String boardTitle) {
        validateNullContents(boardTitle);
        validateEmptyContents(boardTitle);
        validateMaximumLength(boardTitle);
        this.boardTitle = boardTitle;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    private void validateNullContents(String boardTitle) {
        if (boardTitle == null)
            throw new BoardTitleNullException();
    }

    private void validateEmptyContents(String boardTitle) {
        if (boardTitle.isEmpty())
            throw new BoardTitleEmptyException();
    }

    private void validateMaximumLength(String boardTitle) {
        // 개발
        if (boardTitle.length() > MAXIMUM_CONTENT_LENGTH)
            throw new BoardTitleFormatException();
    }
}
