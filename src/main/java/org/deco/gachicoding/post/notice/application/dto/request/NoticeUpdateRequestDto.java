package org.deco.gachicoding.post.notice.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NoticeUpdateRequestDto implements PostRequestDto {

    @NotNull(message = "F0001")
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @NotNull(message = "F0001")
    private Long notIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    private String notTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    private String notContents;

    @Builder
    public NoticeUpdateRequestDto(String userEmail, Long notIdx, String notTitle, String notContents) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContents = notContents;
    }
}
