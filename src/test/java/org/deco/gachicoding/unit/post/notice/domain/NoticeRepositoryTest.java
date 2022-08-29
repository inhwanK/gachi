package org.deco.gachicoding.unit.post.notice.domain;

import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    UserRepository userRepository;

    String notTitle = "공지사항 테스트 제목";
    String notContents = "공지사항 테스트 내용";
    String keyword = "병아리";

    @Test
    @DisplayName("공지사항을 저장한다.")
    void save_savedNotice_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();

        Notice savedTestNotice = noticeRepository.save(notice);

        // when
        Notice savedActualNotice = noticeRepository.findNoticeByIdx(savedTestNotice.getNotIdx())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."));

        // then
        assertThat(savedTestNotice.getNotIdx()).isEqualTo(savedActualNotice.getNotIdx());
    }

    @Test
    @DisplayName("공지사항을 저장하면 자동으로 생성 날짜가 주입된다.")
    void save_SavedNoticeWithCreatedDate_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();

        // when
        Notice savedTestNotice = noticeRepository.save(notice);

        // then
        assertThat(savedTestNotice).isNotNull();
        assertThat(savedTestNotice.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("공지사항 리스트를 최신순으로 가져온다.")
    public void find_findAllNoticeByLatestOrder_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .author(savedTestUser)
                    .notTitle(notTitle)
                    .notContents(notContents)
                    .build();

            noticeRepository.save(notice);
        }

        // when
        List<Notice> savedTestNotices = noticeRepository.findAllNoticeByKeyword("",PageRequest.of(0, 10));

        // then
        assertThat(savedTestNotices).isNotNull();
        assertThat(savedTestNotices.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("검색어로 공지사항 리스트를 검색한다.")
    public void find_findAllNoticeByKeyword_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .author(savedTestUser)
                    .notTitle(notTitle)
                    .notContents(notContents)
                    .build();

            noticeRepository.save(notice);
        }

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle + keyword)
                .notContents(notContents)
                .build();

        noticeRepository.save(notice);

        // when
        List<Notice> savedTestNotices = noticeRepository.findAllNoticeByKeyword(keyword,PageRequest.of(0, 10));

        // then
        assertThat(savedTestNotices).isNotNull();
        assertThat(savedTestNotices.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("오직 활성화 된 공지사항만 가져온다.")
    public void find_findNoticeByOnlyEnabled_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .notLocked(true)
                .build();

        noticeRepository.save(notice);

        // when
        Notice savedTestNotices = noticeRepository.findEnableNoticeByIdx(notice.getNotIdx())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."));

        // then
        assertThat(savedTestNotices).isNotNull();
        assertThat(savedTestNotices.getNotLocked()).isTrue();
    }

    @Test
    @DisplayName("비 활성화 된 공지사항에 접근하면 예외가 발생한다.")
    public void find_findNoticeByOnlyEnabled_Exception() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .notLocked(false)
                .build();

        // when
        noticeRepository.save(notice);

        // then
        assertThatThrownBy(() ->
                noticeRepository.findEnableNoticeByIdx(notice.getNotIdx())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("활성화, 비 활성화 공지사항 무엇이든 가져온다.")
    public void find_findNoticeByDisabledAndEnable_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .notLocked(false)
                .build();

        noticeRepository.save(notice);

        // when
        Notice savedTestNotices = noticeRepository.findNoticeByIdx(notice.getNotIdx())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."));

        // then
        assertThat(savedTestNotices).isNotNull();
        assertThat(savedTestNotices.getNotLocked()).isFalse();
    }

    @Test
    @DisplayName("인덱스로 공지사항을 삭제한다.")
    public void delete_deletedNoticeByNoticeIndex_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Notice notice = Notice.builder()
                .author(savedTestUser)
                .notTitle(notTitle)
                .notContents(notContents)
                .build();

        Long savedNoticeIdx = noticeRepository.save(notice).getNotIdx();

        // when
        Notice savedNotice = noticeRepository.findNoticeByIdx(savedNoticeIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."));

        assertThat(savedNotice).isNotNull();

        noticeRepository.delete(savedNotice);

        // then
        assertThatThrownBy(() ->
                noticeRepository.findNoticeByIdx(savedNoticeIdx)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 입니다."))
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
