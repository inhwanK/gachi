package org.deco.gachicoding.post.notice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.notice.NoticeInactiveException;
import org.deco.gachicoding.exception.post.notice.NoticeNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.post.notice.application.dto.NoticeDtoAssembler;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    @Transactional(rollbackFor = Exception.class)
    public Long registerNotice(
            NoticeSaveRequestDto dto
    ) {
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference() 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        // 이부분은 우리가 정의한 키워드를 통해 쿼리를 날린다. 요구사항이 이메일 -> 닉네임 으로 변경될 경우 변경이 필요해진다.
        // findByUserEmail을 통해 유저 정보를 가져오는 코드는 Service내에 여럿 존재한다.(중복된다)
        // 결합도가 높다고 할 수 있다. private한 메소드로 빼버릴까?
//        User writer = userRepository.findByUserEmail(dto.getUserEmail())
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        // =>
//        User writer = getWriterInfo(dto.getUserEmail());

        Notice notice = noticeRepository.save(createNotice(dto));

        // 에러에 취약 할까?
        Long notIdx = notice.getNotIdx();
        String notContent = notice.getNotContents();

//        if (!dto.isNullTags())
//            tagService.registerBoardTag(notIdx, dto.getTags(), NOTICE);

        // updateContent 부분을 extractImgSrc안으로 옮기자
        // 그러기 위해선 post 엔티티들을 계층타입으로 묶는
        // Post.interface가 있어야 할듯 => updateContent 메서드를 가지는 추상 클래스로 만들면 좋을듯

        // 김인환 - 완전 동의함. 기존의 NOTICE 필드 삭제하고 일단 문자열로 넣음

        notice.updateContent(
                fileService.extractPathAndS3Upload(notIdx, "NOTICE", notContent)
        );

        return notice.getNotIdx();
    }

    private Notice createNotice(NoticeSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());

        return NoticeDtoAssembler.notice(user, dto);
    }

    @Transactional
    public List<NoticeResponseDto> getNoticeList(NoticeListRequestDto dto) {
//        List<NoticeResponseDto> noticeList =
//                NoticeDtoAssembler.noticeResponseDtos(noticeRepository.findAllNoticeByKeyword(keyword, pageable));

//        noticeList.forEach(
//                noticeResponseDto ->
//                        tagService.getTags(noticeResponseDto.getNotIdx(), NOTICE, noticeResponseDto)
//        );

        return NoticeDtoAssembler.noticeResponseDtos(noticeRepository.findAllNoticeByKeyword(dto.getKeyword(), dto.getPageable()));
    }

    @Transactional
    public NoticeResponseDto getNoticeDetail(NoticeDetailRequestDto dto) {
        // 이부분도 중복된다 하지만 findById는 Repository에서 기본적으로 제공하는 키워드이기 때문에 변경의 여지가 적다
//        Notice notice = noticeRepository.findById(notIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//        NoticeResponseDto noticeDetail = NoticeDtoAssembler.noticeResponseDto(getNoticeInfo(notIdx));

//        tagService.getTags(notIdx, NOTICE, noticeDetail);

        Notice notice = findNotice(dto.getNotIdx());

        if (!notice.getNotLocked())
            throw new NoticeInactiveException();

        return NoticeDtoAssembler.noticeResponseDto(notice);
    }

    @Transactional
    public NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto) {
        Notice notice = findNotice(dto.getNotIdx());

        if (!notice.getNotLocked())
            throw new NoticeInactiveException();

        User user = findAuthor(dto.getUserEmail());

        notice.hasSameAuthor(user);

        notice.updateTitle(dto.getNotTitle());

        notice.updateContent(dto.getNotContents());

        return NoticeDtoAssembler.noticeResponseDto(notice);
    }

    // 활성 -> 비활성
    @Transactional
    public void disableNotice(NoticeBasicRequestDto dto) {
        Notice notice = findNotice(dto.getNotIdx());

        User user = findAuthor(dto.getUserEmail());

        notice.hasSameAuthor(user);

        notice.disableNotice();
    }

    // 비활성 -> 활성
    @Transactional
    public void enableNotice(NoticeBasicRequestDto dto) {
        Notice notice = findNotice(dto.getNotIdx());

        User user = findAuthor(dto.getUserEmail());

        notice.hasSameAuthor(user);

        notice.enableNotice();
    }

    @Transactional
    public void removeNotice(NoticeBasicRequestDto dto) {
        Notice notice = findNotice(dto.getNotIdx());

        User user = findAuthor(dto.getUserEmail());

        notice.hasSameAuthor(user);

        noticeRepository.delete(notice);
    }

    private Notice findNotice(Long notIdx) {
        return noticeRepository.findNoticeByIdx(notIdx)
                .orElseThrow(NoticeNotFoundException::new);
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
