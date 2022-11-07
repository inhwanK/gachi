package org.deco.gachicoding.file.domain.vo;

import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FilePath {

    private static final String PATH_FORMAT = "%s/%d/%s";

    protected FilePath() {}

    @Comment("저장된 파일 url")
    @Column(name = "file_path", columnDefinition = "TEXT", unique = true, nullable = false)
    private String filePath;

    // 유효성 검증을 어떤 걸 해야 할지 모르겠다...
    public FilePath(String articleCategory, Long articleIdx, SaveFileName saveFileName) {
        filePath = String.format(
                PATH_FORMAT,
                articleCategory,
                articleIdx,
                saveFileName.getSaveFilename()
        );
    }

    public String getFilePath() {
        return filePath;
    }

    // 메서드 이름 컨벤션 적용 하자
    public boolean isEquals(String path) {
        return filePath.equals(path);
    }
}
