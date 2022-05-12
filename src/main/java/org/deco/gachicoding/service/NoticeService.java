package org.deco.gachicoding.service;

import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface NoticeService {
    Optional<Notice> findById(Long notIdx);

    Long registerNotice(NoticeSaveRequestDto dto);

    Page<NoticeResponseDto> getNoticeList(String keyword, Pageable pageable);

    NoticeResponseDto getNoticeDetail(Long notIdx);

    NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto);

    ResponseEntity<ResponseState> disableNotice(Long notIdx);

    ResponseEntity<ResponseState> enableNotice(Long notIdx);

    ResponseEntity<ResponseState> removeNotice(Long notIdx);
}
