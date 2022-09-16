package org.deco.gachicoding.post.notice.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class NoticeSaveRequest {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    @ApiModelProperty(value = "공지사항 제목", required = true, example = "긴급 공지입니다.")
    private String notTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    @ApiModelProperty(value = "공지사항 내용", required = true, example = "안녕하세요 운영자A입니다.")
    private String notContent;

//    @NotNull
//    @ApiModelProperty(value = "공지사항 내용", required = true, example = "안녕하세요 운영자A입니다.")
//    private String notContents;

    @Nullable
    @ApiModelProperty(value = "상단 고정 여부", required = false, example = "true")
    private Boolean notPin;

//    @Nullable
//    @ApiModelProperty(value = "태그 목록", required = false, example = "운영")
//    private List<String> tags;

    private NoticeSaveRequest() {}

    @Builder
    public NoticeSaveRequest(
            String userEmail,
            String notTitle,
            String notContent,
            Boolean notPin
    ) {
        this.userEmail = userEmail;
        this.notTitle = notTitle;
        this.notContent = notContent;
        this.notPin = notPin;
    }
}
