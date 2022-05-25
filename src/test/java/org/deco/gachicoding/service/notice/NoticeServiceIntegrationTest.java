package org.deco.gachicoding.service.notice;

import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.dto.notice.NoticeResponseDto;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.notice.NoticeUpdateRequestDto;
import org.deco.gachicoding.service.NoticeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NoticeServiceIntegrationTest {

    @Autowired
    NoticeService noticeService;

    Long noticeIdx;

    String notTitle = "공지사항 테스트 제목 고양이 병아리";
    String notContent = "공지사항 테스트 내용 강아지 병아리";
    Boolean notPin = false;

    @BeforeEach
    void before() {
        NoticeSaveRequestDto entity = NoticeSaveRequestDto.builder()
//                .userIdx(Long.valueOf(1))
                .notTitle(notTitle)
                .notContent(notContent)
                .notPin(notPin)
                .build();

        noticeIdx = noticeService.registerNotice(entity);
    }

    @AfterEach
    void after() {
        if (noticeIdx != null) {
            noticeService.removeNotice(this.noticeIdx);
        }
        noticeIdx = null;
    }

    @Test
    @DisplayName("공지사항_작성")
    void Notice_Integration_Testcase_1() {
        Optional<Notice> notice = noticeService.findById(noticeIdx);

        assertEquals(notTitle, notice.get().getNotTitle());
        assertEquals(notContent, notice.get().getNotContent());
    }

    @Test
    @DisplayName("공지사항_리스트_조회")
    public void Notice_Integration_Testcase_2() {
        String keyword = "병아리";

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "notIdx"));

        Page<NoticeResponseDto> noticeList = noticeService.getNoticeList(keyword, pageable);

        assertEquals(noticeList.getTotalElements(), 1);
    }

    // 같은 비즈니스 로직의 다른 사용법을 테스트 케이스로 작성한 것..
    // 필요한 테스트 케이스 일까(통합 시켜도 될까)?
    @Test
    @DisplayName("검색어로_공지사항_검색")
    public void Notice_Integration_Testcase_3() {
        String keyword = "병아리";

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "notIdx"));

        Page<NoticeResponseDto> searchNotice = noticeService.getNoticeList(keyword, pageable);
        assertEquals(searchNotice.getContent().get(0).getNotTitle(), notTitle);
        assertEquals(searchNotice.getContent().get(0).getNotContent(), notContent);
    }

    @Test
    @DisplayName("인덱스로_공지사항_수정")
    public void Notice_Integration_Testcase_4() {
        String updateTitle = "공지사항 수정된 테스트 제목";
        String updateContent = "공지사항 수정된 테스트 내용";

        NoticeUpdateRequestDto updateNotice = NoticeUpdateRequestDto.builder()
                .notIdx(noticeIdx)
                .notTitle(updateTitle)
                .notContent(updateContent)
                .build();

        noticeService.modifyNotice(updateNotice);

        Notice notice = noticeService.findById(noticeIdx).get();

        assertNotEquals(notTitle, notice.getNotTitle());
        assertEquals(updateTitle, notice.getNotTitle());

        assertNotEquals(notContent, notice.getNotContent());
        assertEquals(updateContent, notice.getNotContent());
    }

    @Test
    @DisplayName("인덱스로_공지사항_비활성화")
    public void Notice_Integration_Testcase_5() {
        noticeService.disableNotice(noticeIdx);

        assertThrows(IllegalArgumentException.class, () -> noticeService.getNoticeDetail(noticeIdx));
    }

    @Test
    @DisplayName("인덱스로_공지사항_활성화")
    public void Notice_Integration_Testcase_6() {
        noticeService.enableNotice(noticeIdx);

        Optional<Notice> notice = noticeService.findById(noticeIdx);

        assertEquals(notice.get().getNotActivated(), true);
    }

    @Test
    @DisplayName("공지사항_삭제")
    public void Notice_Integration_Testcase_7() {
        noticeService.removeNotice(noticeIdx);
        assertTrue(noticeService.findById(noticeIdx).isEmpty());
        noticeIdx = null;
    }
}
