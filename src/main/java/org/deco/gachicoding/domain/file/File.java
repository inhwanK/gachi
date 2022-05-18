package org.deco.gachicoding.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileIdx;

    private Long boardIdx;
    private String boardCategory;
    private String origFilename;
    private String saveFilename;
    private Long fileSize;
    private String fileExt;
    private String filePath;
    private Boolean fileActivated;

    private LocalDateTime fileRegdate;
//    private LocalDateTime fileModdate;

    @Builder
    public File(Long fileIdx, Long boardIdx, String boardCategory, String origFilename, String saveFilename, Long fileSize, String fileExt, String filePath, Boolean fileActivated, LocalDateTime fileRegdate) {
        this.fileIdx = fileIdx;
        this.boardIdx = boardIdx;
        this.boardCategory = boardCategory;
        this.origFilename = origFilename;
        this.saveFilename = saveFilename;
        this.fileSize = fileSize;
        this.fileExt = fileExt;
        this.filePath = filePath;
        this.fileActivated = fileActivated;
        this.fileRegdate = fileRegdate;
    }
}
