package org.deco.gachicoding.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.notice.Notice;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponseDto {

    private Long notIdx;
    private Long userIdx;
    private String userNick;
    private String userPicture;
    private String notTitle;
    private String notContent;
    private int notViews;
    private boolean notPin;
    private LocalDateTime notRegdate;

    @Builder
    public NoticeResponseDto(Notice notice) {
        this.notIdx = notice.getNotIdx();
        this.userIdx = notice.getUser().getUserIdx();
        this.userNick = notice.getUser().getUserNick();
        this.userPicture = notice.getUser().getUserPicture();
        this.notTitle = notice.getNotTitle();
        this.notContent = notice.getNotTitle();
        this.notViews = notice.getNotViews();
        this.notPin = notice.isNotPin();
        this.notRegdate = notice.getNotRegdate();
    }
}
