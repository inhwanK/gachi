package org.deco.gachicoding.user.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.deco.gachicoding.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserAuthenticationDto implements UserDetails {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String userEmail;
    private String password;
    private String userNick;
    private List<GrantedAuthority> roles;

    public UserAuthenticationDto(@JsonProperty("userEmail") String userEmail,
                                 @JsonProperty("password") String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    public UserAuthenticationDto(User user, List<GrantedAuthority> roles) {
        this.userEmail = user.getUserEmail();
        this.password = user.getUserPassword();
        this.userNick = user.getUserNick();
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    public String getUserNick() {
        return userNick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String toString() {
        return "UserAuthenticationDto{" +
                "userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                ", userNick='" + userNick + '\'' +
                '}';
    }
}
