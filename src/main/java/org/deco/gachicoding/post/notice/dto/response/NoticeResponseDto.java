//package org.deco.gachicoding.post.notice.dto.response;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.deco.gachicoding.post.notice.domain.Notice;
//import org.deco.gachicoding.user.domain.User;
//import org.deco.gachicoding.tag.dto.TagResponse;
//import org.deco.gachicoding.tag.dto.response.TagResponseDto;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class NoticeResponseDto implements TagResponse {
//    private Long notIdx;
//
//    private String userEmail;
//    private String userNick;
//
//    private String notTitle;
//    private String notContent;
//    private Long notViews;
//    private LocalDateTime notRegdate;
//
//    private List<TagResponseDto> tags;
//
//    @Builder
//    public NoticeResponseDto(Notice notice) {
//        this.notIdx = notice.getNotIdx();
//        setWriterInfo(notice.getWriter());
//
//        this.notTitle = notice.getNotTitle();
//        this.notContent = notice.getNotContent();
//        this.notViews = notice.getNotViews();
//        this.notRegdate = notice.getNotRegdate();
//    }
//
//    private void setWriterInfo(User user) {
//        this.userEmail = user.getUserEmail();
//        this.userNick = user.getUserNick();
//    }
//
//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }
//}