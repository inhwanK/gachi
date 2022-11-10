package org.deco.gachicoding.post.board.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.exception.post.board.BoardAlreadyActiveException;
import org.deco.gachicoding.exception.post.board.BoardAlreadyInactiveException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.board.domain.vo.BoardContents;
import org.deco.gachicoding.post.board.domain.vo.BoardTitle;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Queue;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "board")
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

    @Column(name = "board_views", nullable = false)
    @ColumnDefault("0")
    private Long boardViews;

    @Column(name = "board_locked", nullable = false)
    @ColumnDefault("true")
    private Boolean boardLocked;

    @JsonManagedReference
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Builder
    public Board(
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
        this.boardTitle = new BoardTitle(boardTitle);
        this.boardContents = new BoardContents(boardContents);
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

    public void updateTitle(String updateTitle) {
        this.boardTitle = boardTitle.update(updateTitle);
    }

    public void updateContent(String updateContents) {
        this.boardContents = boardContents.update(updateContents);
    }
}
