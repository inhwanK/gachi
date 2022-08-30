package org.deco.gachicoding.unit.post.notice.domain;

import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.deco.gachicoding.exception.StatusEnum.*;

public class NoticeTest {

    @Test
    @DisplayName("100자 이하의 제목을 가진 공지사항을 생성할 수 있다.")
    void create_NoticeMaximumLengthUnderTitle_Success() {
        // given
        String notTitle = "a".repeat(100);
        String notContents = "테스트 공지사항 내용";
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("100자 초과의 제목을 가진 공지사항을 생성할 수 없다.")
    void create_NoticeMaximumLengthOverTitle_Exception() {
        // given
        String notTitle = "a".repeat(101);
        String notContents = "테스트 공지사항 내용";
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(MAXIMUM_LENGTH_OVER_TITLE);
    }

    @Test
    @DisplayName("제목이 널인 공지사항을 생성할 수 없다.")
    void create_NoticeNullTitle_Exception() {
        // given
        String notTitle = null;
        String notContents = "테스트 공지사항 내용";
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(NULL_TITLE);
    }

    @Test
    @DisplayName("제목이 공백인 공지사항을 생성할 수 없다.")
    void create_NoticeEmptyTitle_Exception() {
        // given
        String notTitle = "";
        String notContents = "테스트 공지사항 내용";
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_TITLE);
    }

    @Test
    @DisplayName("10000자 이하의 내용을 가진 공지사항을 생성할 수 있다.")
    void create_NoticeMaximumLengthUnderContents_Success() {
        // given
        String notTitle = "테스트 공지사항 제목";
        String notContents = "a".repeat(10000);
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("10000자 초과의 내용을 가진 공지사항을 생성할 수 없다.")
    void create_NoticeMaximumLengthOverContents_Exception() {
        // given
        String notTitle = "테스트 공지사항 제목";
        String notContents = "a".repeat(10001);
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(MAXIMUM_LENGTH_OVER_CONTENTS);
    }

    @Test
    @DisplayName("내용이 널인 공지사항을 생성할 수 없다.")
    void create_NoticeNullContents_Exception() {
        // given
        String notTitle = "테스트 공지사항 제목";
        String notContents = null;
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(NULL_CONTENTS);
    }

    @Test
    @DisplayName("내용이 공백인 공지사항을 생성할 수 없다.")
    void create_NoticeEmptyContents_Exception() {
        // given
        String notTitle = "테스트 공지사항 제목";
        String notContents = "";
        User author = UserFactory.user();

        // when, then
        assertThatCode(() -> NoticeFactory.mockNotice(1L, author, notTitle, notContents))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(EMPTY_CONTENTS);
    }

    @Test
    @DisplayName("자신이 작성한 공지사항인지 확인한다.")
    void create_NoticeAuthorMe_Success() {
        // given
        User author = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, null);

        // when, then
        assertThatCode(() -> notice.hasSameAuthor(author))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("자신이 작성한 공지사항인지 확인한다. 아니라면 예외가 발생한다.")
    void create_NoticeAuthorMe_Exception() {
        // given
        User author = UserFactory.user();
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, null);

        // when, then
        assertThatCode(() -> notice.hasSameAuthor(user))
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(INVALID_AUTH_USER);
    }

    @Test
    @DisplayName("공지사항을 비활성 상태로 변경한다.")
    void create_NoticeDisableState_Success() {
        // given
        User author = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, true);

        // when, then
        assertThatCode(() -> notice.disableNotice())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("공지사항을 비활성 상태로 변경한다, 이미 비활성 상태라면 예외가 발생한다.")
    void create_NoticeDisableState_Exception() {
        // given
        User author = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, false);

        // when, then
        assertThatCode(() -> notice.disableNotice())
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(ALREADY_INACTIVE);
    }

    @Test
    @DisplayName("공지사항을 활성 상태로 변경한다.")
    void create_NoticeEnableState_Success() {
        // given
        User author = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, false);

        // when, then
        assertThatCode(() -> notice.enableNotice())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("공지사항을 활성 상태로 변경한다, 이미 활성 상태라면 예외가 발생한다.")
    void create_NoticeEnableState_Exception() {
        // given
        User author = UserFactory.user();
        User user = UserFactory.user();

        Notice notice = NoticeFactory.mockNotice(1L, author, true);

        // when, then
        assertThatCode(() -> notice.enableNotice())
                .isInstanceOf(ApplicationException.class)
                .extracting("statusEnum")
                .isEqualTo(ALREADY_ACTIVE);
    }

    @Test
    @DisplayName("공지사항의 제목과 내용을 변경한다.")
    void create_NoticeUpdate_Success() {
        // given
        User author = UserFactory.user();
        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String afterNotTitle = "테스트 공지사항 제목 수정 후";

        String beforeNotContents = "테스트 공지사항 내용 수정 전";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        Notice notice = NoticeFactory.mockNotice(1L, author, beforeNotTitle, beforeNotContents);

        // when
        notice.update(afterNotTitle, afterNotContents);

        // then
        assertThat(notice.getNotTitle()).isEqualTo(afterNotTitle);
        assertThat(notice.getNotContents()).isEqualTo(afterNotContents);
    }

    @Test
    @DisplayName("공지사항의 제목을 변경한다.")
    void create_NoticeUpdateTitle_Success() {
        // given
        User author = UserFactory.user();
        String beforeNotTitle = "테스트 공지사항 제목 수정 전";
        String afterNotTitle = "테스트 공지사항 제목 수정 후";

        Notice notice = NoticeFactory.mockNotice(1L, author, beforeNotTitle, "테스트 공지사항 내용");

        // when
        notice.updateTitle(afterNotTitle);

        // then
        assertThat(notice.getNotTitle()).isEqualTo(afterNotTitle);
    }

    @Test
    @DisplayName("공지사항의 내용을 변경한다.")
    void create_NoticeUpdateContents_Success() {
        // given
        User author = UserFactory.user();
        String beforeNotContents = "테스트 공지사항 내용 수정 전";
        String afterNotContents = "테스트 공지사항 내용 수정 후";

        Notice notice = NoticeFactory.mockNotice(1L, author, "테스트 공지사항 제목", beforeNotContents);

        // when, then
        notice.updateContent(afterNotContents);

        // then
        assertThat(notice.getNotContents()).isEqualTo(afterNotContents);
    }

}
