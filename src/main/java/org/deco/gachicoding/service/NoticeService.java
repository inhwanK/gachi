package org.deco.gachicoding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.notice.NoticeRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.notice.NoticeBasicRequestDto;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    String NOTICE = "NOTICE";

    @Transactional(rollbackFor = Exception.class)
    public Long registerNotice(NoticeSaveRequestDto dto) throws Exception {
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Notice notice = noticeRepository.save(dto.toEntity(writer));

        Long notIdx = notice.getNotIdx();
        String notContent = notice.getNotContent();

        if (dto.getTags() != null)
            tagService.registerBoardTag(notIdx, dto.getTags(), NOTICE);

        try {
            notice.updateContent(fileService.extractImgSrc(notIdx, notContent, NOTICE));
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Notice Content");
            e.printStackTrace();
            // throw해줘야 Advice에서 예외를 감지 함
            throw e;
        }

        return notIdx;
    }

    @Transactional
    public Page<NoticeResponseDto> getNoticeList(String keyword, Pageable pageable) {
        Page<NoticeResponseDto> noticeList =
                noticeRepository.findByNotContentContainingIgnoreCaseAndNotActivatedTrueOrNotTitleContainingIgnoreCaseAndNotActivatedTrueOrderByNotIdxDesc(keyword, keyword, pageable).map(entity -> new NoticeResponseDto(entity));

        noticeList.forEach(
                noticeResponseDto ->
                        tagService.getTags(noticeResponseDto.getNotIdx(), NOTICE, noticeResponseDto)
        );

        return noticeList;
    }

    @Transactional
    public NoticeResponseDto getNoticeDetail(Long notIdx) {
        Notice notice = noticeRepository.findById(notIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        tagService.getTags(notIdx, NOTICE, noticeDetail);

        return noticeDetail;
    }

    @Transactional
    public NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto) {
        Notice notice = noticeRepository.findById(dto.getNotIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        isSameWrite(notice, user);

        notice.updateTitle(dto.getNotTitle());

        notice.updateContent(dto.getNotContent());

        NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                .notice(notice)
                .build();

        return noticeDetail;
    }

    // 활성 -> 비활성
    // noticeRepository에서 파인드 할때 activated - false 인 애들만 가져오게 하는게 더 좋을지도..?
    @Transactional
    public ResponseEntity<ResponseState> disableNotice(NoticeBasicRequestDto dto) {
        Notice notice = noticeRepository.findById(dto.getNotIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        isSameWrite(notice, user);

        notice.disableNotice();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    // 비활성 -> 활성
    @Transactional
    public ResponseEntity<ResponseState> enableNotice(NoticeBasicRequestDto dto) {
        // 이부분도 중복된다 private한 메소드로 빼버릴까? 좀 뇌절 같기도...
        Notice notice = noticeRepository.findById(dto.getNotIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        isSameWrite(notice, user);

        notice.enableNotice();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

//    @Transactional
//    public ResponseEntity<ResponseState> removeBoard(Long boardIdx) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        boardRepository.delete(board);
//
//        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
//    }

    private void isSameWrite(Notice notice, User user) {
        if (!notice.isWriter(user)) {
            throw new CustomException(INVALID_AUTH_USER);
        }
    }
}
