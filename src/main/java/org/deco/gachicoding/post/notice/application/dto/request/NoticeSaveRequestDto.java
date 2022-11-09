package org.deco.gachicoding.post.notice.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
public class NoticeSaveRequestDto {
    @NotNull
//    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
//    @ApiModelProperty(value = "공지사항 제목", required = true, example = "긴급 공지입니다.")
    private String notTitle;

    @NotNull
//    @ApiModelProperty(value = "공지사항 내용", required = true, example = "안녕하세요 운영자A입니다.")
    private String notContents;

    @Nullable
//    @ApiModelProperty(value = "상단 고정 여부", required = false, example = "true")
    private Boolean notPin;

//    @Nullable
//    @ApiModelProperty(value = "태그 목록", required = false, example = "운영")
//    private List<String> tags;

    @Builder
    public NoticeSaveRequestDto(
            String userEmail,
            String notTitle,
            String notContents,
            Boolean notPin
            /*, List<String> tags*/
    ) {
        this.userEmail = userEmail;
        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notPin = notPin;
//        this.tags = tags;
    }

//    public boolean isNullTags() {
//        return (tags == null || tags.isEmpty()) ? true : false;
//    }
}
