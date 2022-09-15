package org.deco.gachicoding.unit.post.board.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.common.factory.board.BoardFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.exception.post.board.*;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.board.application.BoardService;
import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.presentation.RestBoardController;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardSaveRequest;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardUpdateRequest;
import org.deco.gachicoding.post.board.presentation.dto.response.BoardResponse;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestBoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)     // jpaAuditingHandler
@WithMockUser
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @Test
    @DisplayName("사용자는 게시물을 작성할 수 있다.")
    void write_writeBoardWithUser_Success() throws Exception {
        // given
        String userEmail = "gachicoding@test.com";
        String boardTitle = "테스트 게시물 제목";
        String boardContents = "테스트 게시물 내용";
        String boardCategory = "자유";

        BoardSaveRequest request = BoardFactory.mockBoardSaveRequest(userEmail, boardTitle, boardContents, boardCategory);

        given(boardService.registerBoard(any(BoardSaveRequestDto.class)))
                .willReturn(1L);

        // when
        ResultActions perform = mockMvc.perform(post("/api/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is2xxSuccessful());

        verify(boardService, times(1))
                .registerBoard(any(BoardSaveRequestDto.class));
    }

    @Test
    @DisplayName("활성화 된 게시물이 존재하는 경우 게시물의 목록을 가져온다.")
    void read_readAllEnableList_Success() throws Exception {
        // given
        User user = UserFactory.user();

        BoardResponseDto boardResponseDto1 = BoardFactory.mockBoardResponseDto(1L, user, true);
        BoardResponseDto boardResponseDto2 = BoardFactory.mockBoardResponseDto(2L, user, true);
        BoardResponseDto boardResponseDto3 = BoardFactory.mockBoardResponseDto(3L, user, true);

        List<BoardResponseDto> boardResponseDtos = List.of(
                boardResponseDto1,
                boardResponseDto2,
                boardResponseDto3
        );

        List<BoardResponse> boardResponses = List.of(
                BoardFactory.mockBoardResponse(boardResponseDto1),
                BoardFactory.mockBoardResponse(boardResponseDto2),
                BoardFactory.mockBoardResponse(boardResponseDto3)
        );

        given(boardService.getBoardList(any(BoardListRequestDto.class)))
                .willReturn(boardResponseDtos);

        // when
        ResultActions perform = mockMvc.perform(get("/api/board/list")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(boardResponses)));

        verify(boardService, times(1))
                .getBoardList(any(BoardListRequestDto.class));
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하지 않는 경우 빈배열을 가져온다.")
    void read_readNotExistList_Success() throws Exception {
        // given
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();

        List<BoardResponse> boardResponses = new ArrayList<>();

        given(boardService.getBoardList(any(BoardListRequestDto.class)))
                .willReturn(boardResponseDtos);

        // when
        ResultActions perform = mockMvc.perform(get("/api/board/list")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(boardResponses)));

        verify(boardService, times(1))
                .getBoardList(any(BoardListRequestDto.class));
    }
    
    @Test
    @DisplayName("활성화 된 게시물이 존재하는 경우 게시물 내용을 가져온다.")
    void read_readEnableDetail_Success() throws Exception {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        BoardResponseDto boardResponseDto = BoardFactory.mockBoardResponseDto(boardIdx, user, true);

        given(boardService.getBoardDetail(any(BoardDetailRequestDto.class)))
                .willReturn(boardResponseDto);

        BoardResponse boardResponse = BoardFactory.mockBoardResponse(boardResponseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/api/board/{boardIdx}", boardIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(boardResponse)));

        verify(boardService, times(1))
                .getBoardDetail(any(BoardDetailRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 접근할 경우 예외가 발생한다.")
    void read_readNotExistDetail_Exception() throws Exception {
        // given
        Long boardIdx = 1L;

        given(boardService.getBoardDetail(any(BoardDetailRequestDto.class)))
                .willThrow(new BoardNotFoundException());

        // when
        ResultActions perform = mockMvc.perform(get("/api/board/{boardIdx}", boardIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("해당하는 게시물을 찾을 수 없습니다."));

        verify(boardService, times(1))
                .getBoardDetail(any(BoardDetailRequestDto.class));
    }

    @Test
    @DisplayName("비 활성화 된 게시물에 접근할 경우 예외가 발생한다.")
    void read_readDisableDetail_Exception() throws Exception {
        // given
        Long boardIdx = 1L;

        given(boardService.getBoardDetail(any(BoardDetailRequestDto.class)))
                .willThrow(new BoardInactiveException());

        // when
        ResultActions perform = mockMvc.perform(get("/api/board/{boardIdx}", boardIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("비활성 처리 된 게시물입니다."));

        verify(boardService, times(1))
                .getBoardDetail(any(BoardDetailRequestDto.class));
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 수정할 수 있다.")
    void modify_modifyBoard_Success() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        BoardResponseDto boardResponseDto = BoardFactory.mockBoardResponseDto(boardIdx, user, true);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willReturn(boardResponseDto);

        BoardResponse boardResponse = BoardFactory.mockBoardResponse(boardResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(boardResponse)));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistBoard_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardNotFoundException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("해당하는 게시물을 찾을 수 없습니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("비 활성화 된 게시물을 수정할 경우 예외가 발생한다.")
    public void modify_modifyDisableBoard_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardInactiveException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("비활성 처리 된 게시물입니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 게시물 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistUser_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new UserNotFoundException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("해당하는 사용자를 찾을 수 없습니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물 수정 시 요청자와 작정자가 다를 경우 예외가 발생한다.")
    public void modify_modifyDifferentAuthor_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new UserUnAuthorizedException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("권한이 없는 사용자입니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물 수정 시 제목이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistTitle_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, null, boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardTitleNullException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("게시물의 제목이 널이어서는 안됩니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물 수정 시 제목이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyTitle_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardContents = "Test Board Modified Contents";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, "", boardContents);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardTitleEmptyException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("게시물의 제목이 공백이어서는 안됩니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물 수정 시 내용이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistContents_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, null);

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardContentsNullException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("게시물의 내용이 널이어서는 안됩니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물 수정 시 내용이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyContents_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long boardIdx = 1L;
        String boardTitle = "Test Board Modified Title";

        BoardUpdateRequest request = BoardFactory.mockBoardUpdateRequest(user.getUserEmail(), boardIdx, boardTitle, "");

        given(boardService.modifyBoard(any(BoardUpdateRequestDto.class)))
                .willThrow(new BoardContentsEmptyException());

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("게시물의 내용이 공백이어서는 안됩니다."));

        verify(boardService, times(1))
                .modifyBoard(any(BoardUpdateRequestDto.class));
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 비활성화할 수 있다.")
    public void disable_disableAuthorMe_Success() throws Exception {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(boardService)
                .disableBoard(any(BoardBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/disable/{boardIdx}", boardIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isNoContent());

        verify(boardService, times(1))
                .disableBoard(any(BoardBasicRequestDto.class));
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 활성화할 수 있다.")
    public void enable_enableAuthorMe_Success() throws Exception {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(boardService)
                .enableBoard(any(BoardBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(put("/api/board/enable/{boardIdx}", boardIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isNoContent());

        verify(boardService, times(1))
                .enableBoard(any(BoardBasicRequestDto.class));
    }

    @Test
    @DisplayName("사용자는 게시물을 삭제한다.")
    public void delete_deleteAuthorMe_Success() throws Exception {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(boardService)
                .removeBoard(any(BoardBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(delete("/api/board/{boardIdx}", boardIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isNoContent());

        verify(boardService, times(1))
                .removeBoard(any(BoardBasicRequestDto.class));
    }
}
