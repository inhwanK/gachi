package org.deco.gachicoding.domain.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.answer.Answer;
import org.deco.gachicoding.domain.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_q")
public class Question {
    @Id
    @Column(name = "qs_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qsIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx", insertable = false, updatable = false)
    @JsonBackReference
    private List<Answer> answers = new ArrayList<>();

    @Column(name = "qs_title")
    private String qsTitle;

    @Column(name = "qs_content")
    private String qsContent;

    @Column(name = "qs_error")
    private String qsError;

    @Column(name = "qs_category")
    private String qsCategory;

    @Column(name = "qs_solve")
    private boolean qsSolve;

    @Column(name = "qs_activated")
    private boolean qsActivated;

    @Column(name = "qs_regdate")
    private LocalDateTime qsRegdate;

    @Builder
    public Question(User user, String qsTitle, String qsContent, String qsError, String qsCategory, boolean qsSolve, boolean qsActivated, LocalDateTime qsRegdate) {
        this.user = user;
        this.qsTitle = qsTitle;
        this.qsContent = qsContent;
        this.qsError = qsError;
        this.qsCategory = qsCategory;
        this.qsSolve = qsSolve;
        this.qsActivated = qsActivated;
        this.qsRegdate = qsRegdate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAnswers(Answer answer) {
        this.answers.add(answer);
    }

    public Question update(String qsTitle, String qsContent, String qsError, String qsCategory) {
        this.qsTitle = qsTitle;
        this.qsContent = qsContent;
        this.qsError = qsError;
        this.qsCategory = qsCategory;
        return this;
    }

    public Question isDisable() {
        this.qsActivated = false;
        return this;
    }

}
