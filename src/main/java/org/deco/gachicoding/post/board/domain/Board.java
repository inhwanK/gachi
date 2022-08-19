package org.deco.gachicoding.post.board.domain;

import lombok.Getter;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.post.board.domain.vo.BoardContents;
import org.deco.gachicoding.post.board.domain.vo.BoardTitle;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Embedded
    private BoardTitle boardTitle;

    @Embedded
    private BoardContents boardContents;
    private String boardCategory;
    private Long boardViews;
    private Boolean boardLocked;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User writer;

    public static Builder builder() {
        return new Builder();
    }

    protected Board() {}

    public Board(Long boardIdx, User writer, BoardTitle boardTitle, BoardContents boardContents, String boardCategory, Long boardViews, Boolean boardLocked) {
        this.boardIdx = boardIdx;
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.boardLocked = boardLocked;
    }

    public void update(String boardTitle, String boardContent) {
        updateTitle(boardTitle);
        updateContent(boardContent);
    }

    public void disableBoard() {
        this.boardLocked = false;
    }

    public void enableBoard() {
        this.boardLocked = true;
    }

    public void updateTitle(String boardTitle) {
        this.boardTitle = new BoardTitle(boardTitle);
    }

    public void updateContent(String boardContent) {
        this.boardContents = new BoardContents(boardContent);
    }

    public static class Builder {

        private Long boardIdx;
        private User writer;
        private BoardTitle boardTitle;
        private BoardContents boardContents;
        private String boardCategory;
        private Long boardViews;
        private Boolean boardLocked;

        public Builder boardIdx(Long boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Builder author(User user) {
            this.writer = user;
            return this;
        }

        public Builder boardTitle(String boardTitle) {
            this.boardTitle = new BoardTitle(boardTitle);
            return this;
        }

        public Builder boardContents(String boardContents) {
            this.boardContents = new BoardContents(boardContents);
            return this;
        }

        public Builder boardCategory(String boardCategory) {
            this.boardCategory = boardCategory;
            return this;
        }

        public Builder boardViews(Long boardViews) {
            this.boardViews = boardViews;
            return this;
        }

        public Builder boardLocked(Boolean boardLocked) {
            this.boardLocked = boardLocked;
            return this;
        }

        public Board build() {
            Board board = new Board(
                    boardIdx,
                    writer,
                    boardTitle,
                    boardContents,
                    boardCategory,
                    boardViews,
                    boardLocked
            );

            return board;
        }
    }
}
