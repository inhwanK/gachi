package org.deco.gachicoding.post.notice.dto;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.user.domain.User;

public class NoticeDtoAssembler {

    private NoticeDtoAssembler() {}

    public static Notice notice(User user, NoticeSaveRequestDto dto) {
        return Notice.builder()
                .author(user)
                .notTitle(dto.getNotTitle())
                .notContents(dto.getNotContents())
                .notPin(dto.getNotPin())
                .build();
    }
}
