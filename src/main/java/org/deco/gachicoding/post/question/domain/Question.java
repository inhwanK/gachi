package org.deco.gachicoding.post.question.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "queIdx", callSuper = false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "gachi_q")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qs_idx")
    private Long queIdx;

    @JsonManagedReference
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY) // 자주 사용될 가능성이 많으므로 EAGER 설정 고려
    private User questioner;

    @JsonBackReference
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY) // 자주 사용될 가능성이 많으므로 EAGER 설정 고려
    private List<Answer> answers = new ArrayList<>();

    @Embedded
    private QuestionTitle queTitle;

    @Embedded
    private QuestionContents queContents;

    @ColumnDefault("false")
    @Column(name = "qs_solved", nullable = false)
    private Boolean queSolved;

    @ColumnDefault("true")
    @Column(name = "qs_enabled", nullable = false)
    private Boolean queEnabled;

    @Builder
    public Question(
            Long queIdx,
            User questioner,
            String queTitle,
            String queContents,
            Boolean queSolved,
            Boolean queEnabled,
            LocalDateTime queCreatedAt,
            LocalDateTime queUpdatedAt
    ) {
        this.queIdx = queIdx;
        this.questioner = questioner;
        this.queTitle = new QuestionTitle(queTitle);
        this.queContents = new QuestionContents(queContents);
        this.queSolved = queSolved;
        this.queEnabled = queEnabled;
        setCreatedAt(queCreatedAt);
        setUpdatedAt(queUpdatedAt);
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
        if (this.queSolved)
            throw new QuestionAlreadySolvedException();
        this.queSolved = true;
    }

    public void hasSameAuthor(User user) {
        if (questioner != user)
            throw new UserUnAuthorizedException();
    }

    public void enableQuestion() {
        if (this.queEnabled)
            throw new QuestionAlreadyActiveException();
        this.queEnabled = true;
    }

    public void disableQuestion() {
        if (!this.queEnabled)
            throw new QuestionAlreadyInactiveException();
        this.queEnabled = false;
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
