package org.deco.gachicoding.unit.post.notice.application;

import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.deco.gachicoding.exception.StatusEnum.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

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
    void write_writeNoticeWithUser_Success() {
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
    public void read_readAllEnableList_Success() {
        // given
        User user = UserFactory.user();
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        NoticeListRequestDto requestDto = NoticeFactory.mockNoticeListRequestDto(keyword, pageable);

        List<Notice> notices = List.of(
                NoticeFactory.mockNotice(1L, user, true),
                NoticeFactory.mockNotice(2L, user, true),
                NoticeFactory.mockNotice(3L, user, true)
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

    @Test
    @DisplayName("활성화 된 공지사항이 존재하지 않는 경우 빈배열을 가져온다.")
    public void read_readNotExistList_Success() {
        // given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        NoticeListRequestDto requestDto = NoticeFactory.mockNoticeListRequestDto(keyword, pageable);

        List<Notice> notices = new ArrayList<>();

        given(noticeRepository.findAllNoticeByKeyword(keyword, pageable))
                .willReturn(notices);

        // when
        List<NoticeResponseDto> responseDtos = noticeService.getNoticeList(requestDto);

        // then
        assertThat(responseDtos).isEmpty();

        // 동등성 비교 : 값만 같은지
        // 동등성을 비교하기 때문에 값이 같아야 함
        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(notices);

        verify(noticeRepository, times(1))
                .findAllNoticeByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하는 경우 공지사항 내용을 가져온다.")
    public void read_readEnableDetail_Success() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();
        NoticeDetailRequestDto requestDto = NoticeFactory.mockNoticeDetailRequestDto(notIdx);

        Notice notice = NoticeFactory.mockNotice(notIdx, user, true);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));

        // when
        NoticeResponseDto responseDto = noticeService.getNoticeDetail(requestDto);

        // then
        // 동등성 비교 : 값만 같은지
        // 동등성을 비교하기 때문에 값이 같아야 함
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(notice);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readNotExistDetail_Exception() {
        // given
        Long notIdx = 1L;
        NoticeDetailRequestDto requestDto = NoticeFactory.mockNoticeDetailRequestDto(notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.getNoticeDetail(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(DATA_NOT_EXIST);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readDisableDetail_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();
        NoticeDetailRequestDto requestDto = NoticeFactory.mockNoticeDetailRequestDto(notIdx);

        Notice notice = NoticeFactory.mockNotice(notIdx, user, false);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));

        // when, then
        assertThatThrownBy(() -> noticeService.getNoticeDetail(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INACTIVE_RESOURCE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 수정할 수 있다.")
    public void modify_modifyNotice_Success() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, afterNotContents);
        Notice afterNotice = NoticeFactory.mockNotice(notIdx, user, afterNotTitle, afterNotContents, true);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        NoticeResponseDto responseDto = noticeService.modifyNotice(updateRequestDto);

        // then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(afterNotice);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistNotice_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String notTitle = "테스트 공지사항 제목";
        String notContents = "테스트 공지사항 내용";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(DATA_NOT_EXIST);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 공지사항을 수정할 경우 예외가 발생한다.")
    public void modify_modifyDisableNotice_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, false);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, afterNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INACTIVE_RESOURCE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 공지사항 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistUser_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, afterNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(USER_NOT_FOUND);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 요청자와 작정자가 다를 경우 예외가 발생한다.")
    public void modify_modifyDifferentAuthor_Exception() {
        // given
        Long notIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");


        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, author, beforeNotTitle, beforeNotContents, true);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, afterNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INVALID_AUTH_USER);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistTitle_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, null, afterNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(NULL_TITLE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyTitle_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotContents = "테스트 공지사항 내용 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, "", afterNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_TITLE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistContents_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, null);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(NULL_CONTENTS);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyContents_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String beforeNotContents = "테스트 공지사항 내용 수정 전";

        Notice beforeNotice = NoticeFactory.mockNotice(notIdx, user, beforeNotTitle, beforeNotContents, true);

        String afterNotTitle = "테스트 공지사항 제목 수정 후";

        NoticeUpdateRequestDto updateRequestDto = NoticeFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, afterNotTitle, "");

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(beforeNotice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_CONTENTS);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 비활성화할 수 있다.")
    public void disable_disableAuthorMe_Success() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, true);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        noticeService.disableNotice(requestDto);

        // then
        assertThat(notice.getNotLocked()).isFalse();

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistNotice_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(DATA_NOT_EXIST);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistUser_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, true);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto("okky@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(USER_NOT_FOUND);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 비활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void disable_disableDifferentAuthor_Exception() {
        // given
        Long notIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Notice notice = NoticeFactory.mockNotice(notIdx, author, true);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INVALID_AUTH_USER);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 비활성화 상태의 공지사항에 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableAlreadyDisabled_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(ALREADY_INACTIVE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 활성화할 수 있다.")
    public void enable_enableAuthorMe_Success() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        noticeService.enableNotice(requestDto);

        // then
        assertThat(notice.getNotLocked()).isTrue();

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistNotice_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(DATA_NOT_EXIST);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistUser_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto("okky@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(USER_NOT_FOUND);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void enable_enableDifferentAuthor_Exception() {
        // given
        Long notIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Notice notice = NoticeFactory.mockNotice(notIdx, author, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INVALID_AUTH_USER);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 활성화 상태의 공지사항에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableAlreadyEnabled_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, true);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(ALREADY_ACTIVE);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("사용자는 공지사항을 삭제한다.")
    public void delete_deleteAuthorMe_Success() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, true);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));
        willDoNothing()
                .given(noticeRepository)
                .delete(any(Notice.class));

        // when
        noticeService.removeNotie(requestDto);

        // then
        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
        verify(noticeRepository, times(1))
                .delete(any(Notice.class));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항을 삭제 요청할 경우 예외가 발생한다.")
    public void delete_deleteNotExistNotice_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(DATA_NOT_EXIST);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 삭제 요청할 경우 예외가 발생한다.")
    public void delete_deleteNotExistUser_Exception() {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(notIdx, user, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto("okky@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(USER_NOT_FOUND);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 삭제 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void delete_deleteDifferentAuthor_Exception() {
        // given
        Long notIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Notice notice = NoticeFactory.mockNotice(notIdx, author, false);
        NoticeBasicRequestDto requestDto = NoticeFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INVALID_AUTH_USER);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }
}
