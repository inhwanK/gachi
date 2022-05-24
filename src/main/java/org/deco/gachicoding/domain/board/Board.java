package org.deco.gachicoding.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
@DynamicInsert
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User writer;
    private String boardTitle;
    private String boardContent;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardPin;
    private Boolean boardActivated;

    @Builder
    public Board(User writer, String boardTitle, String boardContent, Long boardViews, LocalDateTime boardRegdate, Boolean boardPin, Boolean boardActivated) {
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardViews = boardViews;
        this.boardRegdate = boardRegdate;
        this.boardPin = boardPin;
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
}
