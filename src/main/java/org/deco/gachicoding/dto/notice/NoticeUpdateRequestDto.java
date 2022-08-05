package org.deco.gachicoding.dto.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class NoticeUpdateRequestDto {

    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "1")
    private Long notIdx;

    @NotNull
    @ApiModelProperty(value = "공지사항 제목", required = false, example = "수정된 제목")
    private String notTitle;

    @NotNull
    @ApiModelProperty(value = "공지사항 내용", required = false, example = "수정된 내용")
    private String notContent;

    @Builder
    public NoticeUpdateRequestDto(String userEmail, Long notIdx, String notTitle, String notContent) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContent = notContent;
    }
}
