package org.deco.gachicoding.post.notice.application.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "notIdx")
@Getter
public class NoticeResponseDto {

    private Long notIdx;
    private User author;

    private String notTitle;
    private String notContents;
    private Long notViews;
    private Boolean notPin;
    private Boolean notEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public NoticeResponseDto(
            Long notIdx,
            User author,
            String notTitle,
            String notContents,
            Long notViews,
            Boolean notPin,
            Boolean notEnabled,
            LocalDateTime createdAt, 
            LocalDateTime updatedAt
    ) {
        this.notIdx = notIdx;
        this.author = author;
        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notEnabled = notEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}