package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.RoleType;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockUser {
    private MockUser() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long userIdx;
        private String userName = "Test User Name";
        private String userNick = "Test User Nick";
        private String userEmail = "test@gachicoding.com";
        private String userPassword = "Test User Password";
        private boolean userEnabled = true;
        private LocalDateTime userCreatedAt = LocalDateTime.of(2022, 2, 2, 2, 2);
        private RoleType userRole = RoleType.ROLE_USER;


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

        public Builder userEnabled(boolean userEnabled) {
            this.userEnabled = userEnabled;
            return this;
        }

        public Builder userCreatedAt(LocalDateTime userCreatedAt) {
            this.userCreatedAt = userCreatedAt;
            return this;
        }

        public Builder userRole(RoleType userRole) {
            this.userRole = userRole;
            return this;
        }

        public User build() {
            return new User(
                    userIdx,
                    userName,
                    userNick,
                    userEmail,
                    userPassword,
                    userEnabled,
                    userCreatedAt,
                    userRole
            );
        }
    }
}
