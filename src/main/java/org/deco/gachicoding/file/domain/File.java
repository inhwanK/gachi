package org.deco.gachicoding.file.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_idx", columnDefinition = "bigint", nullable = false)
    @Comment("PK")
    private Long fileIdx;

    @Column(name = "article_idx", columnDefinition = "bigint", nullable = false)
    @Comment("게시물 번호")
    private Long articleIdx;

    @Column(name = "article_category", columnDefinition = "varchar(255)", nullable = false)
    @Comment("게시물 카테고리(Board, Notice, Question, Answer)")
    // enum?
    private String articleCategory;

    @Column(name = "origin_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("원본 파일 이름")
    private String originFilename;

    @Column(name = "save_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("저장 파일 이름")
    private String saveFilename;

    @Column(name = "file_ext", columnDefinition = "varchar(20)", nullable = false)
    @Comment("파일 확장자")
    // enum?
    private String fileExt;

    @Column(name = "file_path", columnDefinition = "TEXT", nullable = false)
    @Comment("저장된 파일 url")
    // vo로 빼버릴까?
    private String filePath;

    // User 관계 연결 해야겠지?
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_idx")
//    @JsonManagedReference
//    private User author;

    @Builder
    public File(
            Long fileIdx,
            Long articleIdx,
            String articleCategory,
            String originFilename,
            String saveFilename,
            String fileExt,
            String filePath,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.fileIdx = fileIdx;
        this.articleIdx = articleIdx;
        this.articleCategory = articleCategory;
        this.originFilename = originFilename;
        this.saveFilename = saveFilename;
        this.fileExt = fileExt;
        this.filePath = filePath;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }
}
