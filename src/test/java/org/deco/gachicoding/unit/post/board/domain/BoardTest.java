package org.deco.gachicoding.unit.post.board.domain;

import org.deco.gachicoding.common.factory.post.board.MockBoardFactory;
import org.deco.gachicoding.common.factory.user.MockUserFactory;
import org.deco.gachicoding.exception.post.board.*;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class BoardTest {

    @Test
    @DisplayName("100자 이하의 제목을 가진 게시물을 생성할 수 있다.")
    void create_BoardMaximumLengthUnderTitle_Success() {
        // given
        String boardTitle = "a".repeat(100);
        String boardContents = "테스트 게시물 내용";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, boardContents, boardCategory, true))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("100자 초과의 제목을 가진 게시물을 생성할 수 없다.")
    void create_BoardMaximumLengthOverTitle_Exception() {
        // given
        String boardTitle = "a".repeat(101);
        String boardContents = "테스트 게시물 내용";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, boardContents, boardCategory, true))
                .isInstanceOf(BoardTitleOverMaximumLengthException.class)
                .extracting("message")
                .isEqualTo("게시물 제목이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("제목이 널인 게시물을 생성할 수 없다.")
    void create_BoardNullTitle_Exception() {
        // given
        String boardContents = "테스트 게시물 내용";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, null, boardContents, boardCategory, true))
                .isInstanceOf(BoardTitleNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("제목이 공백인 게시물을 생성할 수 없다.")
    void create_BoardEmptyTitle_Exception() {
        // given
        String boardContents = "테스트 게시물 내용";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, "", boardContents, boardCategory, true))
                .isInstanceOf(BoardTitleEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 제목이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("10000자 이하의 내용을 가진 게시물을 생성할 수 있다.")
    void create_BoardMaximumLengthUnderContents_Success() {
        // given
        String boardTitle = "테스트 게시물 제목";
        String boardContents = "a".repeat(10000);
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, boardContents, boardCategory, true))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("10000자 초과의 내용을 가진 게시물을 생성할 수 없다.")
    void create_BoardMaximumLengthOverContents_Exception() {
        // given
        String boardTitle = "테스트 게시물 제목";
        String boardContents = "a".repeat(10001);
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, boardContents, boardCategory, true))
                .isInstanceOf(BoardContentsOverMaximumLengthException.class)
                .extracting("message")
                .isEqualTo("게시물 내용이 길이 제한을 초과하였습니다.");
    }

    @Test
    @DisplayName("내용이 널인 게시물을 생성할 수 없다.")
    void create_BoardNullContents_Exception() {
        // given
        String boardTitle = "테스트 게시물 제목";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, null, boardCategory, true))
                .isInstanceOf(BoardContentsNullException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 널이어서는 안됩니다.");
    }

    @Test
    @DisplayName("내용이 공백인 게시물을 생성할 수 없다.")
    void create_BoardEmptyContents_Exception() {
        // given
        String boardTitle = "테스트 게시물 제목";
        String boardCategory = "자유";
        User author = MockUserFactory.createUser();

        // when, then
        assertThatCode(() -> MockBoardFactory.mockBoard(1L, author, boardTitle, "", boardCategory, true))
                .isInstanceOf(BoardContentsEmptyException.class)
                .extracting("message")
                .isEqualTo("게시물의 내용이 공백이어서는 안됩니다.");
    }

    @Test
    @DisplayName("자신이 작성한 게시물인지 확인한다.")
    void create_BoardAuthorMe_Success() {
        // given
        User author = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, null);

        // when, then
        assertThatCode(() -> board.hasSameAuthor(author))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("자신이 작성한 게시물인지 확인한다. 아니라면 예외가 발생한다.")
    void create_BoardAuthorMe_Exception() {
        // given
        User author = MockUserFactory.createUser();
        User user = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, null);

        // when, then
        assertThatCode(() -> board.hasSameAuthor(user))
                .isInstanceOf(UserUnAuthorizedException.class)
                .extracting("message")
                .isEqualTo("권한이 없는 사용자입니다.");
    }

    @Test
    @DisplayName("게시물을 비활성 상태로 변경한다.")
    void create_BoardDisableState_Success() {
        // given
        User author = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, true);

        // when, then
        assertThatCode(() -> board.disableBoard())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게시물을 비활성 상태로 변경한다, 이미 비활성 상태라면 예외가 발생한다.")
    void create_BoardDisableState_Exception() {
        // given
        User author = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, false);

        // when, then
        assertThatCode(() -> board.disableBoard())
                .isInstanceOf(BoardAlreadyInactiveException.class)
                .extracting("message")
                .isEqualTo("이미 비활성화 된 게시물 입니다.");
    }

    @Test
    @DisplayName("게시물을 활성 상태로 변경한다.")
    void create_BoardEnableState_Success() {
        // given
        User author = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, false);

        // when, then
        assertThatCode(() -> board.enableBoard())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게시물을 활성 상태로 변경한다, 이미 활성 상태라면 예외가 발생한다.")
    void create_BoardEnableState_Exception() {
        // given
        User author = MockUserFactory.createUser();
        User user = MockUserFactory.createUser();

        Board board = MockBoardFactory.mockBoard(1L, author, true);

        // when, then
        assertThatCode(() -> board.enableBoard())
                .isInstanceOf(BoardAlreadyActiveException.class)
                .extracting("message")
                .isEqualTo("이미 활성화 된 게시물 입니다.");
    }

    @Test
    @DisplayName("게시물의 제목과 내용을 변경한다.")
    void create_BoardUpdate_Success() {
        // given
        User author = MockUserFactory.createUser();
        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String afterBoardTitle = "테스트 게시물 제목 수정 후";
        String afterBoardContents = "테스트 게시물 내용 수정 후";

        String boardCategory = "자유";

        Board board = MockBoardFactory.mockBoard(1L, author, beforeBoardTitle, beforeBoardContents, boardCategory, true);

        // when
        board.update(afterBoardTitle, afterBoardContents);

        // then
        assertThat(board.getBoardTitle()).isEqualTo(afterBoardTitle);
        assertThat(board.getBoardContents()).isEqualTo(afterBoardContents);
    }

    @Test
    @DisplayName("게시물의 제목을 변경한다.")
    void create_BoardUpdateTitle_Success() {
        // given
        User author = MockUserFactory.createUser();
        String beforeBoardTitle = "테스트 게시물 제목 수정 전";
        String boardContents = "테스트 게시물 내용";

        String boardCategory = "자유";

        Board board = MockBoardFactory.mockBoard(1L, author, beforeBoardTitle, boardContents, boardCategory, true);

        String afterBoardTitle = "테스트 게시물 제목 수정 후";

        // when
        board.updateTitle(afterBoardTitle);

        // then
        assertThat(board.getBoardTitle()).isEqualTo(afterBoardTitle);
    }

    @Test
    @DisplayName("게시물의 내용을 변경한다.")
    void create_BoardUpdateContents_Success() {
        // given
        User author = MockUserFactory.createUser();
        String boardTitle = "테스트 게시물 제목";
        String beforeBoardContents = "테스트 게시물 내용 수정 전";

        String boardCategory = "자유";

        Board board = MockBoardFactory.mockBoard(1L, author, boardTitle, beforeBoardContents, boardCategory, true);

        String afterBoardContents = "테스트 게시물 내용 수정 후";

        // when
        board.updateContent(afterBoardContents);

        // then
        assertThat(board.getBoardContents()).isEqualTo(afterBoardContents);
    }
}
