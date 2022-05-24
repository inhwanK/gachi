package org.deco.gachicoding.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.notice.Notice;
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
    //    private Long userIdx; <- User 엔터티와 조인해야하는 컬럼
    private String boardTitle;
    private String boardContent;
    private String boardType;
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private Boolean boardPin;
    private Boolean boardActivated;

    @Builder
    public Board(String boardTitle, String boardContent, String boardType, Long boardViews, LocalDateTime boardRegdate, Boolean boardPin, Boolean boardActivated) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardType = boardType;
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
