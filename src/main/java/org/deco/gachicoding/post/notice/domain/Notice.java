package org.deco.gachicoding.post.notice.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.notice.domain.vo.contents.NoticeContents;
import org.deco.gachicoding.post.notice.domain.vo.contents.NoticeTitle;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.exception.ApplicationException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.deco.gachicoding.exception.StatusEnum.ALREADY_ACTIVE;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notIdx;

    @Embedded
    private NoticeTitle notTitle;

    @Embedded
    private NoticeContents notContents;

    private Long notViews;

    private Boolean notPin;

    private Boolean notActivated;

    // notRegdate -> createdAt
    private LocalDateTime notRegdate;

    // FetchType.EAGER 즉시 로딩
    // 1. 대부분의 JPA 구현체는 가능하면 조인을 사용해서 SQL 한번에 함께 조회하려고 한다.
    // 2. 이렇게 하면, 실제 조회할 때 한방 쿼리로 다 조회해온다.
    // FetchType.LAZY 지연 로딩
    // 1. 로딩되는 시점에 Lazy 로딩 설정이 되어있는 Team 엔티티는 프록시 객체로 가져온다.
    // 2. 후에 실제 객체를 사용하는 시점에 초기화가 된다. DB에 쿼리가 나간다.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User writer;

    public Notice(Long notIdx, User author, NoticeTitle notTitle, NoticeContents notContents, Long notViews, Boolean notPin, Boolean notActivated) {
        this.notIdx = notIdx;
        this.writer = author;
        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notActivated = notActivated;
    }

    public String getWriterNick() {
        return writer.getUserNick();
    }

    public String getWriterEmail() {
        return writer.getUserEmail();
    }

    public String getNotContents() {
        return notContents.getNoticeContents();
    }

    public String getNotTitle() {
        return notTitle.getNoticeTitle();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isWriter(User user) {
        // 이거도 User 객체 스스로가 판단하는 걸로 바꾸자 (User 정보의 정보 전문가는 User 도메인)
        return (this.writer.isMe(user)) ? true : false;
    }

    public void updateTitle(NoticeTitle notTitle) {
        this.notTitle = notTitle;
    }

    public void updateContent(NoticeContents notContents) {
        this.notContents = notContents;
    }

    public void enableNotice() {
        if (this.notActivated)
            throw new ApplicationException(ALREADY_ACTIVE);
        this.notActivated = true;
    }

    public void disableNotice() {
        if (!this.notActivated)
            throw new ApplicationException(ALREADY_ACTIVE);
        this.notActivated = false;
    }

    public static class Builder {

        private Long notIdx;
        private User author;
        private NoticeTitle notTitle;
        private NoticeContents notContents;
        private Long notViews;
        private Boolean notPin;
        private Boolean notActivated;
        private LocalDateTime notRegdate = null;

        public Builder notIdx(Long notIdx) {
            this.notIdx = notIdx;
            return this;
        }

        public Builder author(User user) {
            this.author = user;
            return this;
        }

        public Builder notTitle(String notTitle) {
            this.notTitle = new NoticeTitle(notTitle);
            return this;
        }

        public Builder notContents(String notContents) {
            this.notContents = new NoticeContents(notContents);
            return this;
        }

        public Builder notViews(Long notViews) {
            this.notViews = notViews;
            return this;
        }

        public Builder notPin(Boolean notPin) {
            this.notPin = notPin;
            return this;
        }

        public Builder notActivated(Boolean notActivated) {
            this.notActivated = notActivated;
            return this;
        }

        public Notice build() {
            Notice notice = new Notice(
                    notIdx,
                    author,
                    notTitle,
                    notContents,
                    notViews,
                    notPin,
                    notActivated
                    );

            return notice;
        }
    }
}