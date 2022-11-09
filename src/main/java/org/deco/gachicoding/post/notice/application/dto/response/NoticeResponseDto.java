package org.deco.gachicoding.post.notice.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {

    private Long notIdx;
    private User author;

    private String notTitle;
    private String notContents;
    private Long notViews;
    private Boolean notPin;
    private Boolean notLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<TagResponseDto> tags;

    @Builder
    public NoticeResponseDto(
            Long notIdx,
            User author,
            String notTitle,
            String notContents,
            Long notViews,
            Boolean notPin,
            Boolean notLocked,
            LocalDateTime createdAt, 
            LocalDateTime updatedAt
    ) {
        this.notIdx = notIdx;

        this.author = author;

        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notLocked = notLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }
}