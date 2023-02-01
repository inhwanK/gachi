package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.RoleType;
import org.deco.gachicoding.user.domain.User;

public class UserMockFactory {
    private UserMockFactory() {}

    public static User createUser() {
        return UserMock.builder().build();
    }

    public static User createUser(Long userIdx) {
        return UserMock.builder()
                .userIdx(userIdx)
                .build();
    }

    public static User createUser(Long userIdx, String userEmail, String userPassword) {
        return UserMock.builder()
                .userIdx(userIdx)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();
    }

    public static User createManager(Long userIdx, String userEmail, String userPassword) {
        return UserMock.builder()
                .userIdx(userIdx)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userRole(RoleType.ROLE_MANAGER)
                .build();
    }
}
