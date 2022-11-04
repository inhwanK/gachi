package org.deco.gachicoding.file.domain.vo;

import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OriginFileName {

    protected OriginFileName() {}

    @Column(name = "origin_filename", columnDefinition = "varchar(255)", nullable = false)
    @Comment("원본 파일 이름")
    private String originFilename;

    // 유효성 검증을 어떤 걸 해야 할지 모르겠다...
    public OriginFileName(String originFilename) {
        this.originFilename = originFilename;
    }

    public String getOriginFilename() {
        return originFilename;
    }

}
