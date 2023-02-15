package org.deco.gachicoding.unit.post.notice.application;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.common.factory.post.notice.NoticeMockFactory;
import org.deco.gachicoding.common.factory.user.UserMockFactory;
import org.deco.gachicoding.exception.post.notice.*;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
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

    private static final User author =
            UserMockFactory.createUser(1L, "gachicoding@test.com", "1234");
    private static final User user =
            UserMockFactory.createUser(2L, "okky@test.com", "1234");

    private static final Long notIdx = 1L;
    private static final String notTitle = "Test Notice Title";
    private static final String notContents = "Test Notice Contents";
    private static final String updateNotTitle = "Update Test Notice Title";
    private static final String updateNotContents = "Update Test Notice Contents";

    @Test
    @DisplayName("사용자는 공지사항을 작성할 수 있다.")
    void registerNotice_Success() {

        // given
        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        NoticeSaveRequestDto requestDto = NoticeMockFactory
                .mockNoticeSaveRequestDto(author.getUserEmail(), notTitle, notContents);

        Notice notice = NoticeMockFactory.createNotice(notIdx, author);

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(noticeRepository.save(any(Notice.class)))
                .willReturn(notice);
        given(fileService.extractPathAndS3Upload(notIdx, ArticleType.NOTICE, notContents))
                .willReturn("img");

        // when
        Long notIdx = noticeService.registerNotice(author.getUserEmail(), requestDto);

        // then
        assertThat(notIdx).isNotNull();

        verify(userRepository, times(1))
                .findByUserEmail(requestDto.getUserEmail());
        verify(noticeRepository, times(1))
                .save(any(Notice.class));
    }

    @Test
    @DisplayName("제목의 길이가 100보다 크면 공지사항을 등록할 수 없다.")
    public void write_writeMaximumLengthOverTitle_Exception() {
        // given
        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle("*".repeat(101))
                .notContents(notContents)
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeTitleOverMaximumLengthException.class)
                .extracting("message")
                .isEqualTo("공지사항의 제목이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("내용의 길이가 10000보다 크면 공지사항을 등록할 수 없다.")
    public void write_writeMaximumLengthOverContents_Exception() {
        // given
        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle(notTitle)
                .notContents(notContents.repeat(1000))
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeContentsOverMaximumLengthException.class)
                .extracting("message")
                .isEqualTo("공지사항의 내용이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("제목이 널이면 공지사항을 등록할 수 없다.")
    public void write_writeNullTitle_Exception() {

        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        // given
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle(null)
                .notContents(notContents)
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeTitleNullException.class)
                .extracting("message")
                .isEqualTo("공지사항의 제목이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("제목이 공백이면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyTitle_Exception() {
        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        // given
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle("")
                .notContents(notContents)
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeTitleEmptyException.class)
                .extracting("message")
                .isEqualTo("공지사항의 제목이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("내용이 널이면 공지사항을 등록할 수 없다.")
    public void write_writeNullContents_Exception() {

        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        // given
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle(notTitle)
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeContentsNullException.class)
                .extracting("message")
                .isEqualTo("공지사항의 내용이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("내용이 공백이면 공지사항을 등록할 수 없다.")
    public void write_writeEmptyContents_Exception() {
        User author = UserMockFactory
                .createManager(1L, "gachicoding@test.com", "1234");

        // given
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                .userEmail(author.getUserEmail())
                .notTitle(notTitle)
                .notContents("")
                .build();

        // when
        // then
        assertThatThrownBy(() -> noticeService.registerNotice(author.getUserEmail(), requestDto))
                .isInstanceOf(NoticeContentsEmptyException.class)
                .extracting("message")
                .isEqualTo("공지사항의 내용이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("검색어에 조건에 맞는 모든 공지사항을 조회한다.")
    public void read_readAllEnableList_Success() {
        // given
        String keyword = "";

        Pageable pageable =
                PageRequest.of(0, 10);

        NoticeListRequestDto requestDto =
                NoticeMockFactory.mockNoticeListRequestDto(keyword, pageable);

        List<Notice> notices = List.of(
                NoticeMockFactory.createNotice(1L, author),
                NoticeMockFactory.createNotice(2L, author, false),
                NoticeMockFactory.createNotice(3L, author)
        );

        List<NoticeResponseDto> expectedResponseDtos = List.of(
                NoticeMockFactory.mockNoticeResponseDto(notices.get(0)),
                NoticeMockFactory.mockNoticeResponseDto(notices.get(1)),
                NoticeMockFactory.mockNoticeResponseDto(notices.get(2))
        );

        given(noticeRepository.findAllNoticeByKeyword(keyword, pageable))
                .willReturn(notices);

        // when
        List<NoticeResponseDto> responseDtos =
                noticeService.getNoticeList(requestDto);

        // then
        assertThat(responseDtos).hasSize(3);

        // 동등성 비교 : 값만 같은지
        // 동등성을 비교하기 때문에 값이 같아야 함
        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDtos);

        verify(noticeRepository, times(1))
                .findAllNoticeByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하지 않는 경우 빈배열을 가져온다.")
    public void read_readNotExistList_Success() {
        // given
        String keyword = "";

        Pageable pageable = PageRequest.of(0, 10);

        NoticeListRequestDto requestDto = NoticeMockFactory.mockNoticeListRequestDto(keyword, pageable);

        List<Notice> notices = new ArrayList<>();

        given(noticeRepository.findAllNoticeByKeyword(keyword, pageable))
                .willReturn(notices);

        // when
        List<NoticeResponseDto> responseDtos =
                noticeService.getNoticeList(requestDto);

        // then
        assertThat(responseDtos).isEmpty();

        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(notices);

        verify(noticeRepository, times(1))
                .findAllNoticeByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("활성화 된 공지사항의 세부 내용을 조회한다.")
    public void read_readEnableDetail_Success() {
        // given
        NoticeDetailRequestDto requestDto =
                NoticeMockFactory.mockNoticeDetailRequestDto(1L);
        Notice notice =
                NoticeMockFactory.createDefaultStateNoticeMock(author);
        NoticeResponseDto expectedResponseDto =
                NoticeMockFactory.mockNoticeResponseDto(notice);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));

        // when
        NoticeResponseDto responseDto =
                noticeService.getNoticeDetail(requestDto);

        // then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(1L);
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readNotExistDetail_Exception() {
        // given
        NoticeDetailRequestDto requestDto = NoticeMockFactory.mockNoticeDetailRequestDto(notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.getNoticeDetail(requestDto))
                .isInstanceOf(NoticeNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 공지사항을 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readDisableDetail_Exception() {
        // given
        NoticeDetailRequestDto requestDto = NoticeMockFactory.mockNoticeDetailRequestDto(notIdx);

        Notice notice =
                NoticeMockFactory.createNotice(notIdx, author, false);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));

        // when, then
        assertThatThrownBy(() -> noticeService.getNoticeDetail(requestDto))
                .isInstanceOf(NoticeInactiveException.class)
                .extracting("message")
                .isEqualTo("비활성 처리 된 공지사항입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 수정할 수 있다.")
    public void modify_modifyNotice_Success() {
        // given
        Notice notice =
                NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto =
                NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, updateNotTitle, updateNotContents);
        Notice updateNotice =
                NoticeMockFactory.createNotice(notIdx, author, updateNotTitle, updateNotContents, true);
        NoticeResponseDto expectedResponseDto =
                NoticeMockFactory.mockNoticeResponseDto(updateNotice);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(fileService.compareFilePathAndOptimization(notIdx, ArticleType.NOTICE, updateNotContents))
                .willReturn("Update Test Notice Contents");
        // when
        NoticeResponseDto responseDto =
                noticeService.modifyNotice(updateRequestDto);

        // then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistNotice_Exception() {
        // given
        NoticeUpdateRequestDto updateRequestDto = NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 공지사항을 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 공지사항을 수정할 경우 예외가 발생한다.")
    public void modify_modifyDisableNotice_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, false);

        NoticeUpdateRequestDto updateRequestDto =
                NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, updateNotTitle, updateNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeInactiveException.class)
                .extracting("message")
                .isEqualTo("비활성 처리 된 공지사항입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 공지사항 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistUser_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto = NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, updateNotTitle, updateNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 요청자와 작정자가 다를 경우 예외가 발생한다.")
    public void modify_modifyDifferentAuthor_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto = NoticeMockFactory.mockNoticeUpdateRequestDto(user.getUserEmail(), notIdx, updateNotTitle, updateNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistTitle_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto = NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, null, updateNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(fileService.compareFilePathAndOptimization(notIdx, ArticleType.NOTICE, updateNotContents))
                .willReturn("img");

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeTitleNullException.class)
                .extracting("message")
                .isEqualTo("공지사항의 제목이 널이어서는 안됩니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyTitle_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto =
                NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, "", updateNotContents);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(fileService.compareFilePathAndOptimization(notIdx, ArticleType.NOTICE, updateNotContents))
                .willReturn("Update Test Notice Contents");

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeTitleEmptyException.class)
                .extracting("message")
                .isEqualTo("공지사항의 제목이 공백이어서는 안됩니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistContents_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto =
                NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, updateNotTitle, null);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(fileService.compareFilePathAndOptimization(notIdx, ArticleType.NOTICE, null))
                .willReturn(null);

        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeContentsNullException.class)
                .extracting("message")
                .isEqualTo("공지사항의 내용이 널이어서는 안됩니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyContents_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author, notTitle, notContents, true);

        NoticeUpdateRequestDto updateRequestDto =
                NoticeMockFactory.mockNoticeUpdateRequestDto(author.getUserEmail(), notIdx, updateNotTitle, "");

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        given(fileService.compareFilePathAndOptimization(notIdx, ArticleType.NOTICE, ""))
                .willReturn("");
        // when, then
        assertThatThrownBy(() -> noticeService.modifyNotice(updateRequestDto))
                .isInstanceOf(NoticeContentsEmptyException.class)
                .extracting("message")
                .isEqualTo("공지사항의 내용이 공백이어서는 안됩니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 비활성화할 수 있다.")
    public void disable_disableAuthorMe_Success() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        // when
        noticeService.disableNotice(requestDto);

        // then
        assertThat(notice.getNotEnabled()).isFalse();

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistNotice_Exception() {
        // given
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(NoticeNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 공지사항을 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistUser_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto("anonymous@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 비활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void disable_disableDifferentAuthor_Exception() {
        // given
        Notice notice =
                NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto =
                NoticeMockFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 비활성화 상태의 공지사항에 비활성화 요청할 경우 예외가 발생한다.") // 수정 필요
    public void disable_disableAlreadyDisabled_Exception() {
        // given
        User author = UserMockFactory
                .createUser(1L, "gachicoding@test.com", "1234");
        Notice notice =
                NoticeMockFactory.createNotice(author, false);
        NoticeBasicRequestDto requestDto =
                NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        // when, then
        assertThatThrownBy(() -> noticeService.disableNotice(requestDto))
                .isInstanceOf(NoticeAlreadyInactiveException.class)
                .extracting("message")
                .isEqualTo("이미 비활성화 된 공지사항 입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 활성화할 수 있다.")
    public void enable_enableAuthorMe_Success() {
        // given
        Notice notice =
                NoticeMockFactory.createNotice(notIdx, author, false);
        NoticeBasicRequestDto requestDto =
                NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        // when
        noticeService.enableNotice(requestDto);

        // then
        assertThat(notice.getNotEnabled()).isTrue();

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistNotice_Exception() {
        // given
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(NoticeNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 공지사항을 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistUser_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto("anonymous@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void enable_enableDifferentAuthor_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 활성화 상태의 공지사항에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableAlreadyEnabled_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));

        // when, then
        assertThatThrownBy(() -> noticeService.enableNotice(requestDto))
                .isInstanceOf(NoticeAlreadyActiveException.class)
                .extracting("message")
                .isEqualTo("이미 활성화 된 공지사항 입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("사용자는 공지사항을 삭제한다.")
    public void delete_deleteAuthorMe_Success() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(author));
        willDoNothing()
                .given(noticeRepository)
                .delete(any(Notice.class));

        // when
        noticeService.removeNotice(requestDto);

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
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(author.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.removeNotice(requestDto))
                .isInstanceOf(NoticeNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 공지사항을 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 삭제 요청할 경우 예외가 발생한다.")
    public void delete_deleteNotExistUser_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto("anonymous@test.com", notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> noticeService.removeNotice(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 삭제 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void delete_deleteDifferentAuthor_Exception() {
        // given
        Notice notice = NoticeMockFactory.createNotice(notIdx, author);
        NoticeBasicRequestDto requestDto = NoticeMockFactory.mockNoticeBasicRequestDto(user.getUserEmail(), notIdx);

        given(noticeRepository.findNoticeByIdx(anyLong()))
                .willReturn(Optional.of(notice));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> noticeService.removeNotice(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(noticeRepository, times(1))
                .findNoticeByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }
}
