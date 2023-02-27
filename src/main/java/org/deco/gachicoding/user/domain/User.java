package org.deco.gachicoding.user.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.question.domain.Question;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode(of = "userIdx", callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nick", nullable = false, unique = true)
    private String userNick;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_enabled", nullable = false)
    @ColumnDefault("false")
    private boolean userEnabled;

    @Column(name = "user_role", nullable = false)
    @ColumnDefault("\'ROLE_USER\'")
    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    @OneToMany(mappedBy = "author")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "questioner")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "answerer")
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public User(
            Long userIdx, // 이거 왜필요하지?
            String userName,
            String userNick,
            String userEmail,
            String userPassword,
            boolean userEnabled,
            RoleType userRole,
            LocalDateTime userCreatedAt,
            LocalDateTime userUpdatedAt
    ) {
        this.userIdx = userIdx;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userEnabled = userEnabled;
        this.userRole = userRole;
        setCreatedAt(userCreatedAt);
        setUpdatedAt(userUpdatedAt);
    }

    public User updateNick(String userNick) {
        this.userNick = userNick;
        return this;
    }

    public void enableUser() {
        this.userEnabled = true;
    }

    public void changePassword(String password) {
        this.userPassword = password;
    }
}