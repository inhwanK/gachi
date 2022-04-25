package org.deco.gachicoding.domain.agora;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "gachi_agora")
@DynamicInsert
public class Agora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agoraIdx;
    //    private Long userIdx; <- User 엔터티와 조인해야하는 컬럼
    private String agoraTitle;
    private String agoraContent;
    private String agoraThumbnail;
    private LocalDateTime agoraStartdate;
    private LocalDateTime agoraEnddate;
    private LocalDateTime agoraRegdate;
    private Long agoraViews;

    @Builder
    public Agora(String agoraTitle, String agoraContent, String agoraThumbnail, LocalDateTime agoraStartdate, LocalDateTime agoraEnddate, LocalDateTime agoraRegdate, Long agoraViews) {
        this.agoraTitle = agoraTitle;
        this.agoraContent = agoraContent;
        this.agoraThumbnail = agoraThumbnail;
        this.agoraStartdate = agoraStartdate;
        this.agoraEnddate = agoraEnddate;
        this.agoraRegdate = agoraRegdate;
        this.agoraViews = agoraViews;
    }
}
