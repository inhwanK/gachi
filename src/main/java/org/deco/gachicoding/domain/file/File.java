package org.deco.gachicoding.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "file")
public class File {

    @Id
    private Long fileIdx;

    private Long boardIdx;
    private String boardCategory;
    private String origFilename;
    private String saveFilename;
    private int fileSize;
    private String fileExt;
    private String filePath;
    private boolean fileActivated;

    private LocalDateTime fileRegdate;
//    private LocalDateTime fileModdate;

    @Builder
    public File(Long fileIdx, Long boardIdx, String boardCategory, String origFilename, String saveFilename, int fileSize, String fileExt, String filePath, boolean fileActivated, LocalDateTime fileRegdate) {
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
