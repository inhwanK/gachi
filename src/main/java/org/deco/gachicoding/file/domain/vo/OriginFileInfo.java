package org.deco.gachicoding.file.domain.vo;

import org.deco.gachicoding.file.infrastructure.FileNameSupport;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OriginFileInfo {

    protected OriginFileInfo() {}

    @Column(name = "origin_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("원본 파일 이름")
    private String originFilename;

    @Column(name = "file_ext", columnDefinition = "varchar(20)", nullable = false)
    @Comment("파일 확장자")
    private String originFileExt;

    // 유효성 검증을 어떤 걸 해야 할지 모르겠다...
    public OriginFileInfo(String originFilename) {
        this.originFileExt = FileNameSupport.ExtensionExtractor(originFilename);
        this.originFilename = originFilename;
    }

    public String getOriginFilename() {
        return originFilename;
    }

    public String getOriginFileExt() {
        return originFileExt;
    }

}
