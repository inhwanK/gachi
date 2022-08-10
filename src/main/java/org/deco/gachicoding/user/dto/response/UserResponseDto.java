package org.deco.gachicoding.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.user.domain.UserRole;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long userIdx;
    private String userName;
    private String userNick;
    private String userEmail;
    private String userPassword;
    private LocalDateTime userRegdate;
    private boolean userActivated;
    private boolean userAuth;
    private UserRole userRole;

    public UserResponseDto(User user) {
        this.userIdx = user.getUserIdx();
        this.userName = user.getUserRealName();
        this.userNick = user.getUserNick();
        this.userEmail = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        this.userRegdate = user.getUserRegdate();
        this.userActivated = user.isUserActivated();
        this.userAuth = user.isUserAuth();
        this.userRole = user.getUserRole();
    }
}