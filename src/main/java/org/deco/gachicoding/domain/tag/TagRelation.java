package org.deco.gachicoding.domain.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tag_rel")
public class TagRelation {

    // 테그 테이터들이 태그 * 글갯수로 쌓인다
    // 너무 많이 쌓이는거 같은데
    // 문자열 처리로 합쳐서 넣어 버릴까?
    @Id @Column(name = "rel_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationIdx;
    private String articleCategory;
    private Long articleIdx;

    @ManyToOne
    @JoinColumn(name = "tag_idx")
    private Tag tag;
//    private String tagKeyword;

    @Builder
    public TagRelation(Long articleIdx, Tag tag, String articleCategory, String tagKeyword) {
        this.articleIdx = articleIdx;
        this.tag = tag;
        this.articleCategory = articleCategory;
//        this.tagKeyword = tagKeyword;
    }
}
