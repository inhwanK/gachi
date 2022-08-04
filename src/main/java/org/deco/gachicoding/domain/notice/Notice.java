package org.deco.gachicoding.domain.notice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.response.CustomException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.deco.gachicoding.dto.response.StatusEnum.ALREADY_ACTIVE;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity(name = "notice")
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notIdx;

    private String notTitle;
    private String notContent;
    private Long notViews;
    private Boolean notPin;
    private Boolean notActivated;
    private LocalDateTime notRegdate;

    // FetchType.EAGER 즉시 로딩
    // 1. 대부분의 JPA 구현체는 가능하면 조인을 사용해서 SQL 한번에 함께 조회하려고 한다.
    // 2. 이렇게 하면, 실제 조회할 때 한방 쿼리로 다 조회해온다.
    // FetchType.LAZY 지연 로딩
    // 1. 로딩되는 시점에 Lazy 로딩 설정이 되어있는 Team 엔티티는 프록시 객체로 가져온다.
    // 2. 후에 실제 객체를 사용하는 시점에 초기화가 된다. DB에 쿼리가 나간다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User writer;

    @Builder
    public Notice(User writer, String notTitle, String notContent, Long notViews, Boolean notPin, Boolean notActivated, LocalDateTime notRegdate) {
        this.writer = writer;
        this.notTitle = notTitle;
        this.notContent = notContent;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notActivated = notActivated;
        this.notRegdate = notRegdate;
    }

    public Notice setUser(User user) {
        return this.setUser(user);
    }

    public boolean isWriter(User user) {
        // 이거도 User 객체 스스로가 판단하는 걸로 바꾸자 (User 정보의 정보 전문가는 User 도메인)
        return (this.writer.getUserIdx() == user.getUserIdx()) ? true : false;
    }

    public void updateTitle(String notTitle) {
        this.notTitle = notTitle;
    }

    public void updateContent(String notContent) {
        this.notContent = notContent;
    }

    public void enableNotice() {
        if (this.notActivated)
            throw new CustomException(ALREADY_ACTIVE);
        this.notActivated = true;
    }

    public void disableNotice() {
        if (!this.notActivated)
            throw new CustomException(ALREADY_ACTIVE);
        this.notActivated = false;
    }
}