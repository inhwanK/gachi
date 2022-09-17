package org.deco.gachicoding.acceptance.post.notice;

import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.acceptance.BaseIntegrationTest;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeListRequestDto;
import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NoticeIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private User author;

    private static final String notTitle = "Test Notice Title";
    private static final String notContents = "Test Notice Contents";

    @BeforeEach
    void setUp() {
        author = UserFactory.user();

        userRepository.save(author);

        for (int i = 0; i < 5; i ++) {
            noticeRepository.save(
                    NoticeFactory.mockNotice(author)
            );
        }
    }

    @Test
    @DisplayName("사용자는 공지사항을 작성할 수 있다.")
    void writeNoticeLoginUser_Success() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                notTitle,
                notContents
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("제목의 길이가 100보다 크면 공지사항을 등록할 수 없다.")
    public void writeMaximumLengthOverTitle_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                notTitle.repeat(10),
                notContents
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("F0004"))
                .andExpect(jsonPath("errorMessage").value("notTitle - 제한길이를 초과하였습니다."));
    }

    @Test
    @DisplayName("제목이 널이면 공지사항을 등록할 수 없다.")
    public void writeNullTitle_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                null,
                notContents
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("F0001"))
                .andExpect(jsonPath("errorMessage").value("notTitle - 널이어서는 안됩니다."));
    }

    @Test
    @DisplayName("제목이 공백이면 공지사항을 등록할 수 없다.")
    public void writeEmptyTitle_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                "",
                notContents
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("N1003"))
                .andExpect(jsonPath("errorMessage").value("공지사항의 제목이 공백이어서는 안됩니다."));
    }

    @Test
    @DisplayName("내용의 길이가 10000보다 크면 공지사항을 등록할 수 없다.")
    public void writeMaximumLengthOverContents_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(author.getUserEmail(), notTitle, notContents.repeat(1000));

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("F0004"))
                .andExpect(jsonPath("errorMessage").value("notContent - 제한길이를 초과하였습니다."));
    }

    @Test
    @DisplayName("내용이 널이면 공지사항을 등록할 수 없다.")
    void writeNotExistContents_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                notTitle,
                null
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("F0001"))
                .andExpect(jsonPath("errorMessage").value("notContent - 널이어서는 안됩니다."));
    }

    @Test
    @DisplayName("내용이 공백이면 공지사항을 등록할 수 없다.")
    public void writeEmptyContents_Exception() throws Exception {
        // given
        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(
                author.getUserEmail(),
                notTitle,
                ""
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorCode").value("N1006"))
                .andExpect(jsonPath("errorMessage").value("공지사항의 내용이 공백이어서는 안됩니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 목록을 가져온다.")
    public void readAllEnableList_Success() throws Exception {
        // given
        String keyword = "";

        Pageable pageable = PageRequest.of(0, 10);

        NoticeListRequestDto requestDto = NoticeFactory.mockNoticeListRequestDto(keyword, pageable);

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/list")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
//                .andExpectAll(
//                        jsonPath("$.")
//                )
                .andDo(print());
    }
}
