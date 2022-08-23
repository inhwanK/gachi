package org.deco.gachicoding.unit.user.domain;

import lombok.Builder;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockUser {

    public MockUser() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long userIdx;
        private String userEmail;
        private String userName = "Inhwan Kim";
        private String userNick = "nani_inaning";
        private String userPassword = "1234";
        private boolean userLocked = true;
        private boolean userEnabled = false;
        private LocalDateTime userCreatedAt = LocalDateTime.now();
        private String userRole = "ROLE_USER";

        public Builder userIdx(Long userIdx) {
            this.userIdx = userIdx;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
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

        public Builder setUserRole(String userRole) {
            this.userRole = userRole;
            return this;
        }

        public User build() {
            return new User(
                    userEmail,
                    userName,
                    userNick,
                    userPassword,
                    userLocked,
                    userEnabled,
                    userCreatedAt,
                    userRole
            );
        }

    }

}