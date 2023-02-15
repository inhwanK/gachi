package org.deco.gachicoding.post.notice.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.exception.post.notice.NoticeAlreadyActiveException;
import org.deco.gachicoding.exception.post.notice.NoticeAlreadyInactiveException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.notice.domain.vo.NoticeContents;
import org.deco.gachicoding.post.notice.domain.vo.NoticeTitle;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Getter
@Entity
@Table(name = "notice")
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_idx", columnDefinition = "bigint", nullable = false)
    @Comment("PK")
    private Long notIdx;

    @Embedded
    private NoticeTitle notTitle;

    @Embedded
    private NoticeContents notContents;

    @Column(name = "not_views", nullable = false)
    @ColumnDefault("0") // ddl 수행시 적용되는 default 값, @Column의 columnDefinition 속성보다는 이걸 쓰는게 나을 듯
    private Long notViews;

    @Column(name = "not_pin", nullable = false)
    @ColumnDefault("false")
    private Boolean pin;

    @Column(name = "not_enabled", nullable = false)
    @ColumnDefault("true")
    private Boolean notEnabled;

    // FetchType.EAGER 즉시 로딩
    // 1. 대부분의 JPA 구현체는 가능하면 조인을 사용해서 SQL 한번에 함께 조회하려고 한다.
    // 2. 이렇게 하면, 실제 조회할 때 한방 쿼리로 다 조회해온다.
    // FetchType.LAZY 지연 로딩
    // 1. 로딩되는 시점에 Lazy 로딩 설정이 되어있는 Team 엔티티는 프록시 객체로 가져온다.
    // 2. 후에 실제 객체를 사용하는 시점에 초기화가 된다. DB에 쿼리가 나간다.
    @JsonManagedReference
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    public static Builder builder() {
        return new Builder();
    }

    protected Notice() {}

    public Notice(
            Long notIdx,
            User author,
            NoticeTitle notTitle,
            NoticeContents notContents,
            Long notViews, Boolean pin,
            Boolean notEnabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.notIdx = notIdx;
        this.author = author;
        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.pin = pin;
        this.notEnabled = notEnabled;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public String getNotContents() {
        return notContents.getNoticeContents();
    }

    public String getNotTitle() {
        return notTitle.getNoticeTitle();
    }

    public void hasSameAuthor(User user) {
        if (author != user) {
            throw new UserUnAuthorizedException();
        }
    }

    public void enableNotice() {
        if (this.notEnabled)
            throw new NoticeAlreadyActiveException();
        this.notEnabled = true;
    }

    public void disableNotice() {
        if (!this.notEnabled)
            throw new NoticeAlreadyInactiveException();
        this.notEnabled = false;
    }

    public void update(String notTitle, String notContents) {
        updateTitle(notTitle);
        updateContent(notContents);
    }

    public void updateTitle(String updateTitle) {
        this.notTitle = notTitle.update(updateTitle);
    }

    public void updateContent(String updateContents) {
        this.notContents = notContents.update(updateContents);
    }

    public static class Builder {

        private Long notIdx;
        private User author;
        private NoticeTitle notTitle;
        private NoticeContents notContents;
        private Long notViews;
        private Boolean notPin;
        private Boolean notLocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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

        public Builder notLocked(Boolean notLocked) {
            this.notLocked = notLocked;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Notice build() {
            return new Notice(
                    notIdx,
                    author,
                    notTitle,
                    notContents,
                    notViews,
                    notPin,
                    notLocked,
                    createdAt,
                    updatedAt
                    );
        }
    }
}