package org.deco.gachicoding.file.domain.vo;

import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SaveFileName {

    protected SaveFileName() {}

    @Column(name = "save_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("저장 파일 이름")
    private String saveFilename;

    // 유효성 검증이 어려울 듯
    public SaveFileName(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    public String getSaveFilename() {
        return saveFilename;
    }
}
