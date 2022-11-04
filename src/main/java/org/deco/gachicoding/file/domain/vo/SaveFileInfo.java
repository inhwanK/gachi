package org.deco.gachicoding.file.domain.vo;

import org.deco.gachicoding.file.infrastructure.FileNameSupporter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SaveFileInfo {

    protected SaveFileInfo() {}

    @Column(name = "save_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("저장 파일 이름")
    private String saveFilename;

    @Column(name = "file_ext", columnDefinition = "varchar(20)", nullable = false)
    @Comment("파일 확장자")
    private String saveFileExt;

    // 유효성 검증이 어려울 듯
    public SaveFileInfo(String saveFilename) {
        this.saveFileExt = FileNameSupporter.ExtensionExtractor(saveFilename);
        this.saveFilename = FileNameSupporter.md5(saveFilename) + saveFileExt;
    }

    public String getSaveFilename() {
        return saveFilename;
    }

    public String getSaveFileExt() {
        return saveFileExt;
    }
}
