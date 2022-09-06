package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.User;

public class UserFactory {
    private UserFactory() {}

    public static User user() {
        return MockUser.builder().build();
    }

    public static User user(Long userIdx) {
        return MockUser.builder()
                .userIdx(userIdx)
                .builder();
    }

    public static User user(Long userIdx, String userEmail, String userPassword) {
        return MockUser.builder()
                .userIdx(userIdx)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .builder();
    }
}
