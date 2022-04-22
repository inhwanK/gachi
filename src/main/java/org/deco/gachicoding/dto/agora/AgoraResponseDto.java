package org.deco.gachicoding.dto.agora;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.agora.Agora;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AgoraResponseDto {

    private Long agoraIdx;
    private String agoraTitle;
    private String agoraContent;
    private String agoraThumbnail;
    private LocalDateTime agoraStartdate;
    private LocalDateTime agoraEnddate;
    private LocalDateTime agoraRegdate;
    private Long agoraViews;

    public AgoraResponseDto(Agora agora) {
        this.agoraIdx = agoraIdx;
        this.agoraTitle = agoraTitle;
        this.agoraContent = agoraContent;
        this.agoraThumbnail = agoraThumbnail;
        this.agoraStartdate = agoraStartdate;
        this.agoraEnddate = agoraEnddate;
        this.agoraRegdate = agoraRegdate;
        this.agoraViews = agoraViews;
    }
}
