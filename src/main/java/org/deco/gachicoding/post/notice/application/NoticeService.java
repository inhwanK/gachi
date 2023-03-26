package org.deco.gachicoding.post.notice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.notice.NoticeInactiveException;
import org.deco.gachicoding.exception.post.notice.NoticeNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.post.notice.application.dto.NoticeDtoAssembler;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
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

    @Transactional(rollbackFor = Exception.class)
    public Long registerNotice(
            String userEmail,
            NoticeSaveRequestDto dto
    ) {

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Notice notice = noticeRepository.save(createNotice(user, dto));

        Long notIdx = notice.getNotIdx();
        String notContent = notice.getNotContents();

        notice.updateContent(
                fileService.extractPathAndS3Upload(notIdx, ArticleType.NOTICE, notContent)
        );

        return notIdx;
    }

    private Notice createNotice(User user, NoticeSaveRequestDto dto) {
        return NoticeDtoAssembler.notice(user, dto);
    }

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getNoticeList(NoticeListRequestDto dto) {

        return NoticeDtoAssembler.noticeResponseDtos(
                noticeRepository.findAllNoticeByKeyword(
                        dto.getKeyword(),
                        dto.getPageable()
                )
        );
    }

    @Transactional
    public NoticeResponseDto getNoticeDetail(NoticeDetailRequestDto dto) {

        Notice notice = findNotice(dto.getNotIdx());

        if (!notice.getNotEnabled())
            throw new NoticeInactiveException();

//        tagService.getTags(notIdx, NOTICE, noticeDetail);

        return NoticeDtoAssembler.noticeResponseDto(notice);
    }

    @Transactional
    public NoticeResponseDto modifyNotice(NoticeUpdateRequestDto dto) {
        // 무조건 async
        String updateContents = fileService.compareFilePathAndOptimization(
                dto.getNotIdx(),
                ArticleType.NOTICE,
                dto.getNotContents()
        );

        Notice notice = findNotice(dto.getNotIdx());

        if (!notice.getNotEnabled())
            throw new NoticeInactiveException();

        User user = findAuthor(dto.getUserEmail());

        notice.hasSameAuthor(user);

        notice.updateTitle(dto.getNotTitle());

        notice.updateContent(updateContents);

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
