package org.deco.gachicoding.common.factory.post.notice;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.vo.NoticeContents;
import org.deco.gachicoding.post.notice.domain.vo.NoticeTitle;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockNotice {
    private MockNotice() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long notIdx;
        private User author;

        private NoticeTitle notTitle = new NoticeTitle("Test Notice Title");
        private NoticeContents notContents = new NoticeContents("Test Notice Contents");
        private Long notViews = 0L;
        private Boolean notPin = false;
        private Boolean notLocked = true;
        private LocalDateTime createdAt = LocalDateTime.of(2022, 2, 2, 2, 2);
        private LocalDateTime updatedAt = LocalDateTime.of(2022, 2, 2, 2, 2);

        public Builder notIdx(Long notIdx) {
            this.notIdx = notIdx;
            return this;
        }

        public Builder author(User author) {
            this.author = author;
            return this;
        }

        public  Builder notTitle(String notTitle) {
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
