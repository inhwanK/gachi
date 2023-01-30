package org.deco.gachicoding.post.notice.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NoticeUpdateRequest implements PostRequestDto {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @Email(message = "F0002")
    private String authorEmail;

    @NotNull(message = "F0001")
    private Long notIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    private String notTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    private String notContents;

    private NoticeUpdateRequest() {}

    @Builder
    public NoticeUpdateRequest(
            String userEmail,
            Long notIdx,
            String notTitle,
            String notContents
    ) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContents = notContents;
    }
}
