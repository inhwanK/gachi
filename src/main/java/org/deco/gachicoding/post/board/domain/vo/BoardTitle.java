package org.deco.gachicoding.post.board.domain.vo;

import org.deco.gachicoding.exception.post.board.BoardContentsEmptyException;
import org.deco.gachicoding.exception.post.board.BoardContentsFormatException;
import org.deco.gachicoding.exception.post.board.BoardContentsNullException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoardTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column
    private String boardTitle;

    protected BoardTitle() {}

    public BoardTitle(String boardTitle) {
        validateMaximumLength(boardTitle);
        this.boardTitle = boardTitle;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    private void validateNullContents(String boardTitle) {
        if (boardTitle == null)
            throw new BoardContentsNullException();
    }

    private void validateEmptyContents(String boardTitle) {
        if (boardTitle.isEmpty())
            throw new BoardContentsEmptyException();
    }

    private void validateMaximumLength(String boardTitle) {
        // 개발
        if (boardTitle.length() > MAXIMUM_CONTENT_LENGTH)
            throw new BoardContentsFormatException();
    }
}
