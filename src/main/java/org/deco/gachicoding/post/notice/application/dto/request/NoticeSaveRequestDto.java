package org.deco.gachicoding.post.notice.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
public class NoticeSaveRequestDto {

    @NotNull
    private String userEmail;

    @NotNull
    private String notTitle;

    @NotNull
    private String notContents;

    @Nullable
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
