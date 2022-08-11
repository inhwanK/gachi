package org.deco.gachicoding.post.notice.presentation.dto;

import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;

public class NoticeAssembler {

    private NoticeAssembler() {}

    public static NoticeSaveRequestDto noticeSaveRequestDto(NoticeSaveRequest request) {
        return NoticeSaveRequestDto.builder()
                .userEmail(request.getUserEmail())
                .notTitle(request.getNotTitle())
                .notContents(request.getNotContents())
                .notPin(request.getNotPin())
                .tags(request.getTags())
                .build();
    }
}
