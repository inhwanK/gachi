package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.notice.NoticeRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.BoardService;
import org.deco.gachicoding.service.FileService;
import org.deco.gachicoding.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Notice> findById(Long notIdx) {
        return noticeRepository.findById(notIdx);
    }

    @Override
    @Transactional
    public Long registerNotice(NoticeSaveRequestDto dto) {
        log.info("tried Register {}", "Notice");
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Notice notice = noticeRepository.save(dto.toEntity(user));

        Long noticeIdx = notice.getNotIdx();
        String noticeContent = notice.getNotContent();

        log.info("tried File Upload {}", "Notice");
        fileService.extractImgSrc(noticeIdx, noticeContent, "notice");

        return noticeIdx;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getNoticeList(String keyword, Pageable pageable) {
        Page<Notice> notice = noticeRepository.findByNotContentContainingIgnoreCaseAndNotActivatedTrueOrNotTitleContainingIgnoreCaseAndNotActivatedTrueOrderByNotIdxDesc(keyword, keyword, pageable);

        Page<NoticeResponseDto> noticeList = notice.map(
                result -> new NoticeResponseDto(result)
        );

        return noticeList;
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto getNoticeDetail(Long notIdx) {
        Notice notice = noticeRepository.findByNotIdxAndNotActivatedTrue(notIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeDetail;
    }

    @Override
    @Transactional
    public NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto) {
        Notice notice = findById(dto.getNotIdx())
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        notice = notice.update(dto.getNotTitle(), dto.getNotContent());

        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeDetail;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableNotice(Long notIdx) {
        Notice notice = findById(notIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        notice.disableNotice();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableNotice(Long notIdx) {
        Notice notice = findById(notIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        notice.enableNotice();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeNotice(Long notIdx) {
        Notice notice = findById(notIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        noticeRepository.delete(notice);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }
}
