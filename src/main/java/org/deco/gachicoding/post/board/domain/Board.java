package org.deco.gachicoding.post.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
@DynamicInsert
@DynamicUpdate
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User writer;
    private String boardTitle;
    private String boardContent;
    private String boardCategory;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardActivated;

    @Builder
    public Board(User writer, String boardTitle, String boardContent, String boardCategory, Long boardViews, LocalDateTime boardRegdate, Boolean boardActivated) {
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.boardRegdate = boardRegdate;
        this.boardActivated = boardActivated;
    }

    public Board update(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        return this;
    }

    public Board disableBoard() {
        this.boardActivated = false;
        return this;
    }

    public Board enableBoard() {
        this.boardActivated = true;
        return this;
    }

    // 없애고 그냥 update를 써도 됨
    public Board updateContent(String boardContent) {
        this.boardContent = boardContent;
        return this;
    }
}
