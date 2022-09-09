package org.deco.gachicoding.post.board.domain;

import lombok.Getter;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.exception.post.board.BoardAlreadyActiveException;
import org.deco.gachicoding.exception.post.board.BoardAlreadyInactiveException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.board.domain.vo.BoardContents;
import org.deco.gachicoding.post.board.domain.vo.BoardTitle;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_idx", columnDefinition = "bigint", nullable = false)
    @Comment("PK")
    private Long boardIdx;

    @Embedded
    private BoardTitle boardTitle;

    @Embedded
    private BoardContents boardContents;

    @Column(name = "board_category", columnDefinition = "varchar(20)")
    private String boardCategory;

    @Column(name = "board_views", columnDefinition = "bigint default '0'", nullable = false)
    private Long boardViews;

    @Column(name = "board_locked", columnDefinition = "boolean default 'true'", nullable = false)
    private Boolean boardLocked;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User author;

    public Board(
            Long boardIdx,
            User author,
            BoardTitle boardTitle,
            BoardContents boardContents,
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
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public String getBoardTitle() {
        return boardTitle.getBoardTitle();
    }

    public String getBoardContents() {
        return boardContents.getBoardContents();
    }

    public String getAuthorEmail() {
        return author.getUserEmail();
    }

    public String getAuthorNick() {
        return author.getUserNick();
    }

    public void hasSameAuthor(User user) {
        if (author != user) {
            throw new UserUnAuthorizedException();
        }
    }

    public void enableBoard() {
        if (this.boardLocked)
            throw new BoardAlreadyActiveException();
        this.boardLocked = true;
    }

    public void disableBoard() {
        if (!this.boardLocked)
            throw new BoardAlreadyInactiveException();
        this.boardLocked = false;
    }

    public void update(String boardTitle, String boardContent) {
        updateTitle(boardTitle);
        updateContent(boardContent);
    }

    private void updateTitle(String boardTitle) {
        this.boardTitle = new BoardTitle(boardTitle);
    }

    private void updateContent(String boardContent) {
        this.boardContents = new BoardContents(boardContent);
    }

    protected Board() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long boardIdx;
        private User writer;
        private BoardTitle boardTitle;
        private BoardContents boardContents;
        private String boardCategory;
        private Long boardViews;
        private Boolean boardLocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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
                    writer,
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
