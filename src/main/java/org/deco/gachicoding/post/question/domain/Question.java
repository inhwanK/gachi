package org.deco.gachicoding.post.question.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.post.question.domain.vo.QuestionTitle;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "gachi_q")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qs_idx", columnDefinition = "bigint", nullable = false)
    @Comment("PK")
    private Long queIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User questioner;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx", insertable = false, updatable = false)
    @JsonBackReference
    private List<Answer> answers = new ArrayList<>();

    @Embedded
    private QuestionTitle queTitle;

    @Embedded
    private QuestionContents queContents;

    @Column(name = "qs_solved", nullable = false)
    private Boolean queSolved;

    @Column(name = "qs_locked", nullable = false)
    @ColumnDefault("true")
    private Boolean queLocked;

    @Builder
    public Question(
            User questioner,
            Long queIdx,
            String queTitle,
            String queContents,
            Boolean queSolved,
            Boolean queLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.questioner = questioner;
        this.queIdx = queIdx;
        this.queTitle = new QuestionTitle(queTitle);
        this.queContents = new QuestionContents(queContents);
        this.queSolved = queSolved;
        this.queLocked = queLocked;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public void setUser(User user) {
        this.questioner = user;
    }

    public void setAnswers(Answer answer) {
        this.answers.add(answer);
    }

    public void update(String queTitle, String queContents) {
        updateTitle(queTitle);
        updateContent(queContents);
    }

    public Question toSolve() {
        this.queSolved = true;
        return this;
    }

    public Question isDisable() {
        this.queLocked = false;
        return this;
    }

    public Question isEnable() {
        this.queLocked = true;
        return this;
    }

    public void updateTitle(String queTitle) {
        this.queTitle = new QuestionTitle(queTitle);
    }

    public void updateContent(String queContents) {
        this.queContents = new QuestionContents(queContents);
    }

}
