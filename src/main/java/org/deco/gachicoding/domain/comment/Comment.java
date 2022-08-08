package org.deco.gachicoding.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.user.User;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User writer;

    @Column(name = "comm_content")
    private String commContent;

    @Column(name = "comm_regdate")
    private LocalDateTime commRegdate;

    @Column(name = "comm_activated")
    private Boolean commActivated;

    @Column(name = "article_category")
    private String articleCategory;

    @Column(name = "article_idx")
    private Long articleIdx;

    @Builder
    public Comment (User writer, String commContent, LocalDateTime commRegdate, Boolean commActivated, String articleCategory, Long articleIdx) {
        this.writer = writer;
        this.commContent = commContent;
        this.commRegdate = commRegdate;
        this.commActivated = commActivated;
        this.articleCategory = articleCategory;
        this.articleIdx = articleIdx;
    }

    public Comment update(String commContent) {
        this.commContent = commContent;
        return this;
    }

    public Comment disableBoard() {
        this.commActivated = false;
        return this;
    }

    public Comment enableBoard() {
        this.commActivated = true;
        return this;
    }
}
