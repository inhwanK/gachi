package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.RoleType;
import org.deco.gachicoding.user.domain.User;

public class MockUserFactory {
    private MockUserFactory() {}

    public static User createUser() {
        return MockUser.builder().build();
    }

    public static User createUser(Long userIdx) {
        return MockUser.builder()
                .userIdx(userIdx)
                .build();
    }

    public static User createUser(Long userIdx, String userEmail, String userPassword) {
        return MockUser.builder()
                .userIdx(userIdx)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();
    }

    public static User createManager(Long userIdx, String userEmail, String userPassword) {
        return MockUser.builder()
                .userIdx(userIdx)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userRole(RoleType.ROLE_MANAGER)
                .build();
    }
}
