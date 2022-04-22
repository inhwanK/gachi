package org.deco.gachicoding.dto.agora;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.agora.Agora;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AgoraSaveRequestDto {

    private String agoraTitle;
    private String agoraContent;
    private String agoraThumbnail;
    private LocalDateTime agoraStartdate;
    private LocalDateTime agoraEnddate;
    private LocalDateTime agoraRegdate;
    private Long agoraViews;

    public Agora toEntity() {
        return Agora.builder()
                .agoraTitle(agoraTitle)
                .agoraContent(agoraContent)
                .agoraThumbnail(agoraThumbnail)
                .agoraStartdate(agoraStartdate)
                .agoraEnddate(agoraEnddate)
                .agoraRegdate(agoraRegdate)
                .agoraViews(agoraViews)
                .build();
    }
}
