package org.deco.gachicoding.service.notice;

import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface NoticeService {
    Optional<Notice> findById(Long idx);

    NoticeResponseDto findNoticeDetailById(Long idx);

    Page<NoticeResponseDto> findNoticeByKeyword(String keyword, int page);

    Long registerNotice(NoticeSaveRequestDto dto);

    NoticeResponseDto updateNoticeById(Long idx, NoticeUpdateRequestDto dto);

    Long deleteNoticeById(Long idx);
}
