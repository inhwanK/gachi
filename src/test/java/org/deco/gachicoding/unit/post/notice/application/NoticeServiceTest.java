package org.deco.gachicoding.unit.post.notice.application;

import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.deco.gachicoding.exception.StatusEnum.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileService fileService;

    @Test
    @DisplayName("사용자는 공지사항을 작성할 수 있다.")
    void write_writeNoticeWithLoginUser_Success() {
        // given
        NoticeSaveRequestDto requestDto = NoticeFactory.mockLoginNoticeSaveRequestDto();
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, user);

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));
        given(noticeRepository.save(any(Notice.class)))
                .willReturn(notice);

        // when
        Long notIdx = noticeService.registerNotice(requestDto);

        // then
        assertThat(notIdx).isNotNull();

        // userRepository의 findByUserEmail이 1번 실행되었는지 검사한다.
        verify(userRepository, times(1))
                .findByUserEmail(requestDto.getUserEmail());
        verify(noticeRepository, times(1))
                .save(any(Notice.class));
    }

    @Test
    @DisplayName("사용자가 아니면 공지사항을 작성할 수 없다.")
    void write_writeNoticeWithGuest_Exception() {
        // given
        NoticeSaveRequestDto requestDto = NoticeFactory.mockLoginNoticeSaveRequestDto();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // assertThatThrownBy vs assertThatCode 비교하기 - Blog

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(USER_NOT_FOUND);

        verify(userRepository, times(1))
                .findByUserEmail(requestDto.getUserEmail());
    }

//    @Test
//    @DisplayName("권한이 없는 사용자는 공지사항을 작성할 수 없다.")
//    void write_writeNoticeWithInvalidUser_Exception() {
//    }

    @Test
    @DisplayName("제목의 길이가 500보다 크면 공지사항을 등록할 수 없다.")
    public void write_writeMaximumLengthOverTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("a".repeat(501))
                .notContents("테스트 공지사항 내용")
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(MAXIMUM_LENGTH_OVER_TITLE);
    }

    @Test
    @DisplayName("내용의 길이가 10000보다 크면 공지사항을 등록할 수 없다.")
    public void write_writeMaximumLengthOverContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("테스트 공지사항 제목")
                .notContents("테스트 공지사항 내용".repeat(1000))
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(MAXIMUM_LENGTH_OVER_CONTENTS);
    }

    @Test
    @DisplayName("제목이 없으면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notContents("테스트 공지사항 내용".repeat(1000))
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_OR_NULL_TITLE);
    }

    @Test
    @DisplayName("내용이 없으면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("테스트 공지사항 제목")
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_OR_NULL_CONTENTS);
    }
}
