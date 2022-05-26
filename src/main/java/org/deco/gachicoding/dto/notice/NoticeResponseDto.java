package org.deco.gachicoding.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.file.FileResponseDto;
import org.deco.gachicoding.dto.tag.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponseDto implements ResponseDto {

    private Long notIdx;
    private String userEmail;
    private String userNick;

    private String notTitle;
    private String notContent;
    private int notViews;
    private Boolean notPin;
    private LocalDateTime notRegdate;

    private List<FileResponseDto> files;
    private List<TagResponseDto> tags;

    @Builder
    public NoticeResponseDto(Notice notice) {
        this.notIdx = notice.getNotIdx();
        this.userEmail = notice.getWriter().getUserEmail();
        this.userNick = notice.getWriter().getUserNick();

        this.notTitle = notice.getNotTitle();
        this.notContent = notice.getNotContent();
        this.notViews = notice.getNotViews();
        this.notPin = notice.getNotPin();
        this.notRegdate = notice.getNotRegdate();
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}
