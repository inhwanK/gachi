package org.deco.gachicoding.file.domain;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.file.domain.vo.FilePath;
import org.deco.gachicoding.file.domain.vo.OriginFileInfo;
import org.deco.gachicoding.file.domain.vo.SaveFileName;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    @Embedded
    private OriginFileInfo originFileInfo;

    @Embedded
    private SaveFileName saveFileName;

    @Embedded
    private FilePath filePath;

    public String getFilePath() {
        return filePath.getFilePath();
    }

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
            ObjectMetadata objectMetadata,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.fileIdx = fileIdx;
        this.articleIdx = articleIdx;
        this.articleCategory = articleCategory;

        this.originFileInfo = new OriginFileInfo(
                objectMetadata.getUserMetadata().get("OriginalFileName")
        );
        this.saveFileName = new SaveFileName(
                objectMetadata.getUserMetadata().get("SaveFileName")
        );
        this.filePath = new FilePath(articleCategory, articleIdx, saveFileName);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public boolean compareFilePath(String path) {
        return filePath.isEquals(path);
    }

    public String getOriginFileName() {
        return originFileInfo.getOriginFilename();
    }

    public String getOriginFileExt() {
        return originFileInfo.getOriginFileExt();
    }
}
