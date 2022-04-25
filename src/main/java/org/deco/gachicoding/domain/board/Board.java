package org.deco.gachicoding.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long boardViews;
    private LocalDateTime boardRegdate;
    private boolean boardPin;
    private boolean boardActivated;

    @Builder
    public Board(String boardTitle, String boardContent, Long boardViews, LocalDateTime boardRegdate, boolean boardPin, boolean boardActivated) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardViews = boardViews;
        this.boardRegdate = boardRegdate;
        this.boardPin = boardPin;
        this.boardActivated = boardActivated;
    }
}
