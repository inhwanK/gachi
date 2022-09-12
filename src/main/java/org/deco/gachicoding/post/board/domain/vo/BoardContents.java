package org.deco.gachicoding.post.board.domain.vo;

import org.deco.gachicoding.exception.post.board.BoardContentsEmptyException;
import org.deco.gachicoding.exception.post.board.BoardContentsNullException;
import org.deco.gachicoding.exception.post.board.BoardTitleFormatException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoardContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 10000;

    @Column(name = "board_contents", columnDefinition = "text", nullable = false)
    private String boardContents;

    protected BoardContents() {}

    public BoardContents(String boardContents) {
        validateMaximumLength(boardContents);
        this.boardContents = boardContents;
    }

    public String getBoardContents() {
        return boardContents;
    }

    private void validateNullContents(String boardContents) {
        if (boardContents == null)
            throw new BoardContentsNullException();
    }

    private void validateEmptyContents(String boardContents) {
        if (boardContents.isEmpty())
            throw new BoardContentsEmptyException();
    }

    private void validateMaximumLength(String boardContents) {
        // 개발
        if (boardContents.length() > MAXIMUM_CONTENT_LENGTH)
            throw new BoardTitleFormatException();
    }
}
