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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "user_name")
    private String userRealName;
    private String userNick;
    private String userEmail;
    private String userPassword;
    private LocalDateTime userCreatedAt;
    private boolean userLocked;
    private boolean userEnabled;

    @Builder
    public User(Long userIdx, String userName, String userNick, String userEmail, String userPassword, LocalDateTime userCreatedAt, boolean userLocked, boolean userEnabled) {
        this.userIdx = userIdx;
        this.userRealName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userCreatedAt = userCreatedAt;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
    }

    public User update(String userNick, String userPassword, boolean userActivated, boolean userEnabled) {
        this.userNick = userNick;
        this.userPassword = userPassword;
        this.userLocked = userActivated;
        this.userEnabled = userEnabled;
        return this;
    }

    public void emailAuthenticated() {
        this.userEnabled = true;
    }

    public boolean isMe(User user) {
        // 이거도 User 객체 스스로가 판단하는 걸로 바꾸자 (User 정보의 정보 전문가는 User 도메인)
        return (this.userIdx == user.getUserIdx()) ? true : false;
    }
}