package org.deco.gachicoding.unit.post.notice.application;

import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeListRequestDto;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
        NoticeSaveRequestDto requestDto = NoticeFactory.mockUserNoticeSaveRequestDto();
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, user, null);

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
        NoticeSaveRequestDto requestDto = NoticeFactory.mockGuestNoticeSaveRequestDto();

        given(userRepository.findByUserEmail(null))
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
    @DisplayName("제목의 길이가 100보다 크면 공지사항을 등록할 수 없다.")
    public void write_writeMaximumLengthOverTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("a".repeat(101))
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
                .notContents("a".repeat(10001))
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(MAXIMUM_LENGTH_OVER_CONTENTS);
    }

    @Test
    @DisplayName("제목이 널이면 공지사항을 등록할 수 없다.")
    public void write_writeNullTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notContents("테스트 공지사항 내용")
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(NULL_TITLE);
    }

    @Test
    @DisplayName("제목이 공맥면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("")
                .notContents("테스트 공지사항 내용")
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_TITLE);
    }

    @Test
    @DisplayName("내용이 널이면 공지사항을 등록할 수 없다.")
    public void write_writeNullContents_Exception() {
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
                .isEqualTo(NULL_CONTENTS);
    }

    @Test
    @DisplayName("내용이 공백이면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .notTitle("테스트 공지사항 제목")
                .notContents("")
                .build();

        // when
        // then
        assertThatCode(() -> noticeService.registerNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_CONTENTS);
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하는 경우 공지사항의 목록을 가져온다.")
    public void read_readAllEnableNoticeList_Success() {
        // given
        User user = UserFactory.user();
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        NoticeListRequestDto requestDto = NoticeFactory.mockEmptyKeywordNoticeListRequestDto(keyword, pageable);

        List<Notice> notices = List.of(
                NoticeFactory.mockNotice(1L, user, null),
                NoticeFactory.mockNotice(2L, user, null),
                NoticeFactory.mockNotice(3L, user, null)
        );

        given(noticeRepository.findAllNoticeByKeyword(keyword, pageable))
                .willReturn(notices);

        // when
        List<NoticeResponseDto> responseDtos = noticeService.getNoticeList(requestDto);

        // then
        assertThat(responseDtos).hasSize(3);
        
        // 동등성 비교 : 값만 같은지
        // 동등성을 비교하기 때문에 값이 같아야 함
        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(notices);

        verify(noticeRepository, times(1))
                .findAllNoticeByKeyword(keyword, pageable);
    }
}
