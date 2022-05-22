package org.deco.gachicoding.domain.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "board_tag")
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardTagIdx;
    private Long boardIdx;
    private Long tagIdx;
    private String boardType;

    @Builder
    public BoardTag (Long boardIdx, Long tagIdx, String boardType) {
        this.boardIdx = boardIdx;
        this.tagIdx = tagIdx;
        this.boardType = boardType;
    }
}
