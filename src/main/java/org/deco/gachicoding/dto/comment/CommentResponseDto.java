package org.deco.gachicoding.dto.comment;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commIdx;
    private String userEmail;
    private String userNick;
    private String userPicture;

    private Long parentsIdx;
    private String commContent;
    private LocalDateTime commRegdate;
    private Boolean commActivated;
    private String articleCategory;
    private Long articleIdx;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.commIdx = comment.getCommIdx();
        this.userEmail = comment.getWriter().getUserEmail();
        this.userNick = comment.getWriter().getUserNick();
        this.userPicture = comment.getWriter().getUserPicture();

        this.commContent = comment.getCommContent();
        this.commRegdate = comment.getCommRegdate();
        this.commActivated = comment.getCommActivated();
        this.articleCategory = comment.getArticleCategory();
        this.articleIdx = comment.getArticleIdx();

//        if(isChildComment(comment))
        this.parentsIdx = comment.getParentsIdx();
    }

//    private boolean isChildComment(Comment comment) {
//        if(comment.getParentsIdx() != null)
//            return true;
//        return false;
//    }
}
