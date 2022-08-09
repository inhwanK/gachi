package org.deco.gachicoding.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "user_name")
    private String userRealName;
    private String userNick;
    private String userEmail;
    private String userPassword;
    private LocalDateTime userRegdate;
    private boolean userActivated;
    private boolean userAuth;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    public User(Long userIdx, String userName,  String userNick, String userEmail, String userPassword, LocalDateTime userRegdate, boolean userActivated, boolean userAuth, UserRole userRole) {
        this.userIdx = userIdx;
        this.userRealName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRegdate = userRegdate;
        this.userActivated = userActivated;
        this.userAuth = userAuth;   // 인증여부
        this.userRole = userRole;
    }

    public User update(String userNick, String userPassword, boolean userActivated, boolean userAuth, UserRole userRole) {
        this.userNick = userNick;
        this.userPassword = userPassword;
        this.userActivated = userActivated;
        this.userAuth = userAuth;
        this.userRole = userRole;
        return this;
    }

    public void emailAuthenticated(){
        this.userAuth = true;
    }

    public boolean isMe(User user) {
        // 이거도 User 객체 스스로가 판단하는 걸로 바꾸자 (User 정보의 정보 전문가는 User 도메인)
        return (this.userIdx == user.getUserIdx()) ? true : false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = this.userRole;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>(); // List인 이유 : 여러개의 권한을 가질 수 있다
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
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
}