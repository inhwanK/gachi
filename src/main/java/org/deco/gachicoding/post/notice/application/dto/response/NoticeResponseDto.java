package org.deco.gachicoding.post.notice.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {
    private Long notIdx;

    private String userEmail;
    private String userNick;

    private String notTitle;
    private String notContent;
    private Long notViews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<TagResponseDto> tags;

    @Builder
    public NoticeResponseDto(Long notIdx, String userEmail, String userNick, String notTitle, String notContents, Long notViews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.notIdx = notIdx;

        this.userEmail = userEmail;
        this.userNick = userNick;

        this.notTitle = notTitle;
        this.notContent = notContents;
        this.notViews = notViews;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }
}