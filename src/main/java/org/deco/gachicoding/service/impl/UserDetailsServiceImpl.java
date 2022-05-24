package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.domain.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                UserRole role = user.getUserRole();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toString());
                Collection<GrantedAuthority> authorities = new ArrayList<>(); // List인 이유 : 여러개의 권한을 가질 수 있다
                authorities.add(authority);
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getUserPassword();
            }

            @Override
            public String getUsername() {
                return user.getUserName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        return userDetails;
    }
}
