package org.deco.gachicoding.post.notice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeUpdateResponseDto {

    private String notTitle;
    private String notContent;

    private NoticeUpdateResponseDto() {}

    public NoticeUpdateResponseDto(String notTitle, String notContent) {
        this.notTitle = notTitle;
        this.notContent = notContent;
    }
}
