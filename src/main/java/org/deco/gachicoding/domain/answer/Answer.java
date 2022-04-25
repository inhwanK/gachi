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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "q_idx")
    @JsonManagedReference
    private Question question;

    private String aContent;
    private boolean aSelect;
    private boolean aActivated;
    private LocalDateTime aRegdate;

    @Builder
    public Answer(User user, Question question, String aContent, boolean aSelect, boolean aActivated, LocalDateTime aRegdate) {
        this.user = user;
        this.question = question;
        this.aContent = aContent;
        this.aSelect = aSelect;
        this.aActivated = aActivated;
        this.aRegdate = aRegdate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer update(String aContent) {
        this.aContent = aContent;
        return this;
    }

    public Answer delete() {
        this.aActivated = false;
        return this;
    }
}
