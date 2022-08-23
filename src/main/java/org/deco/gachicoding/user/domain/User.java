package org.deco.gachicoding.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userNick;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean userLocked;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean userEnabled;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime userCreatedAt;

    @Column(nullable = false)
    @ColumnDefault("\'ROLE_USER\'")
    private String userRole;

    @Builder
    public User(Long userIdx, String userName, String userNick, String userEmail, String userPassword, boolean userLocked, boolean userEnabled) {
        this.userIdx = userIdx;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
    }

    public User(String userName, String userNick, String userEmail, String userPassword, boolean userLocked, boolean userEnabled, LocalDateTime userCreatedAt, String userRole) {
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
        this.userCreatedAt = userCreatedAt;
        this.userRole = userRole;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(userIdx, user.getUserIdx());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdx);
    }

    @Override
    public String toString() {
        return "User{" +
                "userIdx=" + userIdx +
                ", userName='" + userName + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userLocked=" + userLocked +
                ", userEnabled=" + userEnabled +
                ", userCreatedAt=" + userCreatedAt +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}