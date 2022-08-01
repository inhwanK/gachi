package org.deco.gachicoding.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.user.UserRole;

@Setter
@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @ApiModelProperty(value = "수정할 비밀번호", required = false, example = "12345")
    private String userPassword;

    @ApiModelProperty(value = "수정할 사용자 별명", required = false, example = "비밀번호:12345")
    private String userNick;

    @ApiModelProperty(value = "수정할 권한", required = false, example = "USER")
    private UserRole userRole;

    @ApiModelProperty(value = "계정 활성 또는 비활성", required = false, example = "false")
    private boolean userActivated;

    @ApiModelProperty(value = "계정 인증여부", required = false, example = "true")
    private boolean userAuth;

    public UserUpdateRequestDto(String userNick, String userPassword, boolean userActivated, boolean userAuth, UserRole userRole) {
        this.userNick = userNick;
        this.userPassword = userPassword;
        this.userActivated = userActivated;
        this.userAuth = userAuth;
        this.userRole = userRole;
    }
}
