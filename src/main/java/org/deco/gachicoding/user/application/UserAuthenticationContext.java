package org.deco.gachicoding.user.application;


import org.deco.gachicoding.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserAuthenticationContext implements UserDetails {

    private final User user;
    private final String userEmail;
    private String password;
    private final Set<GrantedAuthority> authorities;

    public UserAuthenticationContext(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.userEmail = user.getUserEmail();
        this.password = user.getUserPassword();
        this.authorities = (Set<GrantedAuthority>) authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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


}
