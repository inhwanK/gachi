package org.deco.gachicoding.post.question.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyActiveException;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionAlreadySolvedException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
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

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_q")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qs_idx", columnDefinition = "bigint", nullable = false)
    private Long queIdx;

    @JsonManagedReference
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User questioner;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx", insertable = false, updatable = false)
    private List<Answer> answers = new ArrayList<>();

    @Embedded
    private QuestionTitle queTitle;

    @Embedded
    private QuestionContents queContents;

    @ColumnDefault("false")
    @Column(name = "qs_solved", nullable = false)
    private Boolean queSolved;

    @ColumnDefault("true")
    @Column(name = "qs_locked", nullable = false)
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

    public void toSolve() {
        // 이미 해결 상태의 질문은 채택 불가능
        if(this.queSolved)
            throw new QuestionAlreadySolvedException();
        this.queSolved = true;
    }

    public void hasSameAuthor(User user) {
        if (questioner != user)
            throw new UserUnAuthorizedException();
    }

    public void enableQuestion() {
        if (this.queLocked)
            throw new QuestionAlreadyActiveException();
        this.queLocked = true;
    }

    public void disableQuestion() {
        if (!this.queLocked)
            throw new QuestionAlreadyInactiveException();
        this.queLocked = false;
    }

    public String getQueTitle() {
        return queTitle.getQuestionTitle();
    }

    public String getQueContents() {
        return queContents.getQuestionContents();
    }

    public void updateTitle(String updateTitle) {
        queTitle = queTitle.update(updateTitle);
    }

    public void updateContent(String updateContents) {
        queContents = queContents.update(updateContents);
    }

}
