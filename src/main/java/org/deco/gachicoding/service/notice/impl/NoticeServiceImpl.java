package org.deco.gachicoding.service.notice.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.notice.NoticeRepository;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.service.notice.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto findNoticeDetailById(Long idx) {
        Optional<Notice> notice = noticeRepository.findById(idx);
        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice.get())
                .build();
        return noticeDetail;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findNoticeByKeyword(String keyword, int page) {
        Page<Notice> notices = noticeRepository.findByNotContentContainingIgnoreCaseAndNotActivatedTrueOrNotTitleContainingIgnoreCaseAndNotActivatedTrueOrderByNotIdxDesc(keyword, keyword, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "notIdx")));
        Page<NoticeResponseDto> noticeList = notices.map(
                result -> new NoticeResponseDto(result)
        );
        return noticeList;
    }

    @Override
    @Transactional
    public Long registerNotice(NoticeSaveRequestDto dto) {
        Notice notice = dto.toEntity();

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
        notice.setUser(userRepository.getOne(dto.getUserIdx()));

        return noticeRepository.save(notice).getNotIdx();
    }

    @Override
    @Transactional
    public NoticeResponseDto updateNoticeById(Long idx, NoticeUpdateRequestDto dto) {

        Notice notice = noticeRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + idx));

        notice = notice.update(dto.getNotTitle(), dto.getNotContent(), dto.isNotPin());

        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeDetail;
    }

    @Override
    @Transactional
    public Long deleteNoticeById(Long idx) {

        Notice notice = noticeRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + idx));

        return notice.delete().getNotIdx();
    }
}
