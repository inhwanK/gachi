package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockUser {
    private MockUser() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long userIdx;
        private String userName = "테스트 유저 이름";
        private String userNick = "테스트 유저 닉네임";
        private String userEmail = "test@gachicoding.com";
        private String userPassword = "테스트 유저 비밀번호";
        private boolean userLocked = true;
        private boolean userEnabled = true;
        private LocalDateTime userCreatedAt = LocalDateTime.of(2022, 2, 2, 2, 2);
        private String userRole = "이건 잘 모르겠음";

        public Builder userIdx(Long userIdx) {
            this.userIdx = userIdx;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder userNick(String userNick) {
            this.userNick = userNick;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder userPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }

        public Builder userLocked(boolean userLocked) {
            this.userLocked = userLocked;
            return this;
        }

        public Builder userEnabled(boolean userEnabled) {
            this.userEnabled = userEnabled;
            return this;
        }

        public Builder userCreatedAt(LocalDateTime userCreatedAt) {
            this.userCreatedAt = userCreatedAt;
            return this;
        }

        public Builder userRole(String userRole) {
            this.userRole = userRole;
            return this;
        }

        public User builder() {
            return new User(
                    userIdx,
                    userName,
                    userNick,
                    userEmail,
                    userPassword,
                    userLocked,
                    userEnabled,
                    userCreatedAt,
                    userRole
            );
        }
    }
}
