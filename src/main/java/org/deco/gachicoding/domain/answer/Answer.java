package org.deco.gachicoding.domain.answer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_a")
public class Answer {
    @Id
    @Column(name = "as_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx")
    @JsonManagedReference
    private Question question;

    @JoinColumn(name = "as_content")
    private String asContent;

    @JoinColumn(name = "as_select")
    private boolean asSelect;

    @JoinColumn(name = "as_activated")
    private boolean asActivated;

    @JoinColumn(name = "as_regdate")
    private LocalDateTime asRegdate;

    @Builder
    public Answer(User user, Question question, String asContent, boolean asSelect, boolean asActivated, LocalDateTime asRegdate) {
        this.user = user;
        this.question = question;
        this.asContent = asContent;
        this.asSelect = asSelect;
        this.asActivated = asActivated;
        this.asRegdate = asRegdate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer update(String asContent) {
        this.asContent = asContent;
        return this;
    }

    public Answer isDisable() {
        this.asActivated = false;
        return this;
    }
}
