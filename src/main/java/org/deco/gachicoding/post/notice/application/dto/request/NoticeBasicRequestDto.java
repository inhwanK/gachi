package org.deco.gachicoding.post.notice.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class NoticeBasicRequestDto implements PostRequestDto {
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @NotNull
    private Long notIdx;

    @Builder
    public NoticeBasicRequestDto(String userEmail, Long notIdx) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
    }
}
