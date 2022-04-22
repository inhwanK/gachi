package org.deco.gachicoding.domain.question;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "gachiQ")
public class Question {
    @Id
    private Long qIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User user;

    private String qTitle;
    private String qContent;
    private String qError;
    private String qCategory;
    private boolean qSolve;
    private boolean qActivated;
    private LocalDateTime qRegdate;

    @Builder
    public Question(User user, String qTitle, String qContent, String qError, String qCategory, boolean qSolve, boolean qActivated, LocalDateTime qRegdate) {
        this.user = user;
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qError = qError;
        this.qCategory = qCategory;
        this.qSolve = qSolve;
        this.qActivated = qActivated;
        this.qRegdate = qRegdate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question update(String qTitle, String qContent, String qError, String qCategory) {
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qError = qError;
        this.qCategory = qCategory;
        return this;
    }

    public Question delete() {
        this.qActivated = false;
        return this;
    }

}
