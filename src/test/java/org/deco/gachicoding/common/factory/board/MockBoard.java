package org.deco.gachicoding.common.factory.board;

import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.vo.BoardContents;
import org.deco.gachicoding.post.board.domain.vo.BoardTitle;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockBoard {
    private MockBoard() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long boardIdx;
        private User author;

        private BoardTitle boardTitle = new BoardTitle("Test Board Title");
        private BoardContents boardContents = new BoardContents("Test Board Contents");
        private String boardCategory = "자유";
        private Long boardViews = 0L;
        private Boolean boardLocked = true;;
        private LocalDateTime createdAt = LocalDateTime.of(2022, 2, 2, 2, 2);
        private LocalDateTime updatedAt = LocalDateTime.of(2022, 2, 2, 2, 2);

        public Builder boardIdx(Long boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Builder author(User author) {
            this.author = author;
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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Board build() {
            return new Board(
                    boardIdx,
                    author,
                    boardTitle,
                    boardContents,
                    boardCategory,
                    boardViews,
                    boardLocked,
                    createdAt,
                    updatedAt
            );
        }
    }
}
