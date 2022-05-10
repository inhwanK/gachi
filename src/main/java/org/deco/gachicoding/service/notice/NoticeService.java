package org.deco.gachicoding.service.notice;

import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface NoticeService {
    Optional<Notice> findById(Long notIdx);

    Long registerNotice(NoticeSaveRequestDto dto);

    Page<NoticeResponseDto> getNoticeList(String keyword, Pageable pageable);

    NoticeResponseDto getNoticeDetail(Long notIdx);

    NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto);

    void disableNotice(Long notIdx);

    void enableNotice(Long notIdx);

    Long removeNotice(Long notIdx);
}
