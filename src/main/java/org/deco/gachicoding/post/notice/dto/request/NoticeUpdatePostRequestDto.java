package org.deco.gachicoding.post.notice.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class NoticeUpdatePostRequestDto implements PostRequestDto {

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
    private String notContents;

    @Builder
    public NoticeUpdatePostRequestDto(String userEmail, Long notIdx, String notTitle, String notContents) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContents = notContents;
    }
}
