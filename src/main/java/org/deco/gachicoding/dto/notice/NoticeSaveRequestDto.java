package org.deco.gachicoding.dto.notice;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.user.User;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoticeSaveRequestDto {
    @NotNull
    private String userEmail;

    @NotNull
    private String notTitle;

    @NotNull
    private String notContent;

    @Nullable
    private Boolean notPin;

    @Nullable
    private List<String> tags;

    @Builder
    public NoticeSaveRequestDto(String userEmail, String notTitle, String notContent, Boolean notPin) {
        this.userEmail = userEmail;
        this.notTitle = notTitle;
        this.notContent = notContent;
        this.notPin = notPin;
    }

    public Notice toEntity(User writer){
        return Notice.builder()
                .writer(writer)
                .notTitle(notTitle)
                .notContent(notContent)
                .notPin(notPin)
                .build();
    }
}
