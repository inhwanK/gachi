package org.deco.gachicoding.post.answer.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.answer.domain.vo.AnswerContents;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_a")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id
    @Column(name = "as_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ansIdx;

    @JsonManagedReference
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User answerer;

    @JsonManagedReference
    @JoinColumn(name = "qs_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Embedded
    private AnswerContents ansContents;

    @ColumnDefault("false")
    @Column(name = "as_selected", columnDefinition = "boolean", nullable = false)
    private Boolean ansSelected;

    @ColumnDefault("true")
    @Column(name = "as_locked", columnDefinition = "boolean", nullable = false)
    private Boolean ansLocked;

    @Builder
    public Answer(
            User answerer,
            Question question,
            String ansContents,
            Boolean ansSelected,
            Boolean ansLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.answerer = answerer;
        this.question = question;
        this.ansContents = new AnswerContents(ansContents);
        this.ansSelected = ansSelected;
        this.ansLocked = ansLocked;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public Long getQueIdx() {
        return question.getQueIdx();
    }

    public void hasSameAuthor(User user) {
        if (answerer != user) {
            throw new UserUnAuthorizedException();
        }
    }

    public void setUser(User writer) {
        this.answerer = writer;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void update(String updateContent) {
        ansContents = ansContents.update(updateContent);
    }

    public String getAnsContents() {
        return ansContents.getAnswerContents();
    }

    public Answer toSelect() {
        this.ansSelected = true;
        return this;
    }
}