package org.deco.gachicoding.common.factory.user;

import org.deco.gachicoding.user.domain.User;

public class UserFactory {
    private UserFactory() {}

    public static User user() {
        return MockUser.builder().build();
    }
}
