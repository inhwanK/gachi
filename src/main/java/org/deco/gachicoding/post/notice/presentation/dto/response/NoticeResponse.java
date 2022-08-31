package org.deco.gachicoding.post.notice.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponse {
    private Long notIdx;

    private String userEmail;
    private String userNick;

//    private String authorEmail;
//    private String authorNick;

    private String notTitle;
    private String notContent;
    private Boolean notPin;
    private Long notViews;
    private LocalDateTime notRegdate;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;

    @Builder
    public NoticeResponse(Long notIdx, String authorEmail, String authorNick, String notTitle, String notContents, Long notViews, Boolean notPin, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.notIdx = notIdx;

        this.userEmail = authorEmail;
        this.userNick = authorNick;

//        this.authorEmail = authorEmail;
//        this.authorNick = authorNick;

        this.notTitle = notTitle;
        this.notContent = notContents;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notRegdate = createdAt;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
    }
}
