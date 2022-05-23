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

    // 테그 테이터들이 태그 * 글갯수로 쌓인다
    // 너무 많이 쌓이는거 같은데
    // 문자열 처리로 합쳐서 넣어 버릴까?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardTagIdx;
    private String boardType;
    private Long boardIdx;
    private Long tagIdx;
    private String tagKeyword;

    @Builder
    public BoardTag (Long boardIdx, Long tagIdx, String boardType, String tagKeyword) {
        this.boardIdx = boardIdx;
        this.tagIdx = tagIdx;
        this.boardType = boardType;
        this.tagKeyword = tagKeyword;
    }
}
