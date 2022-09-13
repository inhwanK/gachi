package org.deco.gachicoding.unit.post.board.application;

import org.deco.gachicoding.common.factory.board.BoardFactory;
import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.post.board.*;
import org.deco.gachicoding.exception.post.notice.*;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.board.application.BoardService;
import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.repository.BoardRepository;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자는 게시물을 작성할 수 있다.")
    void write_writeBoardWithUser_Success() {
        // given
        User user = UserFactory.user();

        String boardTitle = "테스트 게시물 제목 수정 전";
        String boardContents = "테스트 게시물 내용 수정 전";
        String boardCategory = "자유";

        BoardSaveRequestDto requestDto = BoardFactory.mockBoardSaveRequestDto(user.getUserEmail(), boardTitle, boardContents, boardCategory);

        Board board = BoardFactory.mockBoard(1L, user, null);

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));
        given(boardRepository.save(any(Board.class)))
                .willReturn(board);

        // when
        Long boardIdx = boardService.registerBoard(requestDto);

        // then
        assertThat(boardIdx).isNotNull();

        // userRepository의 findByUserEmail이 1번 실행되었는지 검사한다.
        verify(userRepository, times(1))
                .findByUserEmail(requestDto.getUserEmail());
        verify(boardRepository, times(1))
                .save(any(Board.class));
    }

    @Test
    @DisplayName("사용자가 아니면 게시물을 작성할 수 없다.")
    void write_writeBoardWithGuest_Exception() {
        // given

        String boardTitle = "테스트 게시물 제목 수정 전";
        String boardContents = "테스트 게시물 내용 수정 전";
        String boardCategory = "자유";

        BoardSaveRequestDto requestDto = BoardFactory.mockBoardSaveRequestDto(null, boardTitle, boardContents, boardCategory);

        given(userRepository.findByUserEmail(null))
                .willReturn(Optional.empty());

        // assertThatThrownBy vs assertThatCode 비교하기 - Blog

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(userRepository, times(1))
                .findByUserEmail(requestDto.getUserEmail());
    }

    @Test
    @DisplayName("제목의 길이가 100보다 크면 게시물을 등록할 수 없다.")
    public void write_writeMaximumLengthOverTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardTitle("a".repeat(101))
                .boardContents("테스트 게시물 내용")
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardTitleFormatException.class)
                .extracting("message")
                .isEqualTo("게시물 제목이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("내용의 길이가 10000보다 크면 게시물을 등록할 수 없다.")
    public void write_writeMaximumLengthOverContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardTitle("테스트 게시물 제목")
                .boardContents("a".repeat(10001))
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardContentsFormatException.class)
                .extracting("message")
                .isEqualTo("게시물 내용이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("제목이 널이면 게시물을 등록할 수 없다.")
    public void write_writeNullTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardContents("테스트 게시물 내용")
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardTitleNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("제목이 공백이면 게시물을 등록할 수 없다.")
    public void write_writeEmptyTitle_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardTitle("")
                .boardContents("테스트 게시물 내용")
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardTitleEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("내용이 널이면 게시물을 등록할 수 없다.")
    public void write_writeNullContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardTitle("테스트 게시물 제목")
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardContentsNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("내용이 공백이면 게시물을 등록할 수 없다.")
    public void write_writeEmptyContents_Exception() {
        // given
        User user = UserFactory.user();

        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        BoardSaveRequestDto requestDto = BoardSaveRequestDto.builder()
                .userEmail(user.getUserEmail())
                .boardTitle("테스트 게시물 제목")
                .boardContents("")
                .boardCategory("자유")
                .build();

        // when
        // then
        assertThatCode(() -> boardService.registerBoard(requestDto))
                .isInstanceOf(BoardContentsEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("활성화 된 게시물이 존재하는 경우 게시물의 목록을 가져온다.")
    public void read_readAllEnableList_Success() {
        // given
        User user = UserFactory.user();
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        BoardListRequestDto requestDto = BoardFactory.mockBoardListRequestDto(keyword, pageable);

        List<Board> boards = List.of(
                BoardFactory.mockBoard(1L, user, true),
                BoardFactory.mockBoard(2L, user, true),
                BoardFactory.mockBoard(3L, user, true)
        );

        given(boardRepository.findAllBoardByKeyword(keyword, pageable))
                .willReturn(boards);

        // when
        List<BoardResponseDto> responseDtos = boardService.getBoardList(requestDto);

        // then
        assertThat(responseDtos).hasSize(3);

        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(boards);

        verify(boardRepository, times(1))
                .findAllBoardByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("활성화 된 게시물이 존재하지 않는 경우 빈배열을 가져온다.")
    public void read_readNotExistList_Success() {
        // given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        BoardListRequestDto requestDto = BoardFactory.mockBoardListRequestDto(keyword, pageable);

        List<Board> boards = new ArrayList<>();

        given(boardRepository.findAllBoardByKeyword(keyword, pageable))
                .willReturn(boards);

        // when
        List<BoardResponseDto> responseDtos = boardService.getBoardList(requestDto);

        // then
        assertThat(responseDtos).isEmpty();

        // 동등성 비교 : 값만 같은지
        // 동등성을 비교하기 때문에 값이 같아야 함
        assertThat(responseDtos)
                .usingRecursiveComparison()
                .isEqualTo(boards);

        verify(boardRepository, times(1))
                .findAllBoardByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("활성화 된 게시물이 존재하는 경우 게시물 내용을 가져온다.")
    public void read_readEnableDetail_Success() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();
        BoardDetailRequestDto requestDto = BoardFactory.mockBoardDetailRequestDto(boardIdx);

        Board board = BoardFactory.mockBoard(boardIdx, user, true);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));

        // when
        BoardResponseDto responseDto = boardService.getBoardDetail(requestDto);

        // then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(board);

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 접근할 경우 예외가 발생한다.")
    public void read_readNotExistDetail_Exception() {
        // given
        Long boardIdx = 1L;
        BoardDetailRequestDto requestDto = BoardFactory.mockBoardDetailRequestDto(boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.getBoardDetail(requestDto))
                .isInstanceOf(BoardNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 게시물을 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 게시물에 접근할 경우 예외가 발생한다.")
    public void read_readDisableDetail_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();
        BoardDetailRequestDto requestDto = BoardFactory.mockBoardDetailRequestDto(boardIdx);

        Board board = BoardFactory.mockBoard(boardIdx, user, false);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));

        // when, then
        assertThatThrownBy(() -> boardService.getBoardDetail(requestDto))
                .isInstanceOf(BoardInactiveException.class)
                .extracting("message")
                .isEqualTo("비활성 처리 된 게시물입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 수정할 수 있다.")
    public void modify_modifyBoard_Success() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";
        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, afterBoardContents);
        Board afterBoard = BoardFactory.mockBoard(boardIdx, user, afterBoardTitle, afterBoardContents, boardCategory, true);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        BoardResponseDto responseDto = boardService.modifyBoard(updateRequestDto);

        // then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(afterBoard);

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistBoard_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String boardTitle = "테스트 게시물 제목";
        String boardContents = "테스트 게시물 내용";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, boardTitle, boardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 게시물을 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("비 활성화 된 게시물에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyDisableBoard_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, false);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";
        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, afterBoardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardInactiveException.class)
                .extracting("message")
                .isEqualTo("비활성 처리 된 게시물입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 게시물 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistUser_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";
        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, afterBoardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 수정 시 요청자와 작정자가 다를 경우 예외가 발생한다.")
    public void modify_modifyDifferentAuthor_Exception() {
        // given
        Long boardIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, author, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";
        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, afterBoardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 수정 시 제목이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistTitle_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, null, afterBoardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardTitleNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 널이어서는 안됩니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 수정 시 제목이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyTitle_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardContents = "테스트 게시물 내용 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, "", afterBoardContents);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardTitleEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 공백이어서는 안됩니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 수정 시 내용이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistContents_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, null);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardContentsNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 널이어서는 안됩니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 수정 시 내용이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyContents_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board beforeBoard = BoardFactory.mockBoard(boardIdx, user, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";

        BoardUpdateRequestDto updateRequestDto = BoardFactory.mockBoardUpdateRequestDto(user.getUserEmail(), boardIdx, afterBoardTitle, "");

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(beforeBoard));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.modifyBoard(updateRequestDto))
                .isInstanceOf(BoardContentsEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 공백이어서는 안됩니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 비활성화할 수 있다.")
    public void disable_disableAuthorMe_Success() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        boardService.disableBoard(requestDto);

        // then
        assertThat(board.getBoardLocked()).isFalse();

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistBoard_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.disableBoard(requestDto))
                .isInstanceOf(BoardNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 게시물을 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableNotExistUser_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto("okky@test.com", boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.disableBoard(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("공지사항 비활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void disable_disableDifferentAuthor_Exception() {
        // given
        Long boardIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Board board = BoardFactory.mockBoard(boardIdx, author, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.disableBoard(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 비활성화 상태의 게시물에 비활성화 요청할 경우 예외가 발생한다.")
    public void disable_disableAlreadyDisabled_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, false);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.disableBoard(requestDto))
                .isInstanceOf(BoardAlreadyInactiveException.class)
                .extracting("message")
                .isEqualTo("이미 비활성화 된 게시물 입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물의 작성자는 게시물을 활성화할 수 있다.")
    public void enable_enableAuthorMe_Success() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, false);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        boardService.enableBoard(requestDto);

        // then
        assertThat(board.getBoardLocked()).isTrue();

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 게시물에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistBoard_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.enableBoard(requestDto))
                .isInstanceOf(BoardNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 게시물을 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableNotExistUser_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, false);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto("okky@test.com", boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.enableBoard(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 활성화 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void enable_enableDifferentAuthor_Exception() {
        // given
        Long boardIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Board board = BoardFactory.mockBoard(boardIdx, author, false);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.enableBoard(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("이미 활성화 상태의 게시물에 활성화 요청할 경우 예외가 발생한다.")
    public void enable_enableAlreadyEnabled_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.enableBoard(requestDto))
                .isInstanceOf(BoardAlreadyActiveException.class)
                .extracting("message")
                .isEqualTo("이미 활성화 된 게시물 입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("사용자는 게시물을 삭제한다.")
    public void delete_deleteAuthorMe_Success() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        willDoNothing()
                .given(boardRepository)
                .delete(any(Board.class));

        // when
        boardService.removeBoard(requestDto);

        // then
        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
        verify(boardRepository, times(1))
                .delete(any(Board.class));
    }

    @Test
    @DisplayName("존재하지 않는 게시물을 삭제 요청할 경우 예외가 발생한다.")
    public void delete_deleteNotExistBoard_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.removeBoard(requestDto))
                .isInstanceOf(BoardNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 게시물을 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 삭제 요청할 경우 예외가 발생한다.")
    public void delete_deleteNotExistUser_Exception() {
        // given
        Long boardIdx = 1L;
        User user = UserFactory.user();

        Board board = BoardFactory.mockBoard(boardIdx, user, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto("okky@test.com", boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boardService.removeBoard(requestDto))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("message")
                .isEqualTo("해당하는 사용자를 찾을 수 없습니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }

    @Test
    @DisplayName("게시물 삭제 시 요청자와 작성자가 다르면 예외가 발생한다.")
    public void delete_deleteDifferentAuthor_Exception() {
        // given
        Long boardIdx = 1L;
        User author = UserFactory.user(1L, "gachicoding@test.com", "1234");
        User user = UserFactory.user(2L, "okky@test.com", "1234");

        Board board = BoardFactory.mockBoard(boardIdx, author, true);
        BoardBasicRequestDto requestDto = BoardFactory.mockBoardBasicRequestDto(user.getUserEmail(), boardIdx);

        given(boardRepository.findBoardByIdx(anyLong()))
                .willReturn(Optional.of(board));
        given(userRepository.findByUserEmail(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> boardService.removeBoard(requestDto))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");

        verify(boardRepository, times(1))
                .findBoardByIdx(anyLong());
        verify(userRepository, times(1))
                .findByUserEmail(anyString());
    }
}
