package org.deco.gachicoding.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NoticeUpdateRequestDto {
    private Long notIdx;
    private String notTitle;
    private String notContent;

    @Builder
    public NoticeUpdateRequestDto(Long notIdx, String notTitle, String notContent) {
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContent = notContent;
    }
}