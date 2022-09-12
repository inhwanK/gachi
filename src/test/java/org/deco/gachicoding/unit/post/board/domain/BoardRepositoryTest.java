package org.deco.gachicoding.unit.post.board.domain;

import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.exception.post.board.BoardNotFoundException;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.repository.BoardRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    String boardTitle = "게시물 테스트 제목";
    String boardContents = "게시물 테스트 내용";
    String boardCategory = "자유";
    String keyword = "강아지";

    @Test
    @DisplayName("게시물을 저장한다.")
    void save_saveBoard_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();

        Board savedTestBoard = boardRepository.save(board);

        // when
        Board savedActualBoard = boardRepository.findBoardByIdx(savedTestBoard.getBoardIdx())
                .orElseThrow(BoardNotFoundException::new);

        // then
        assertThat(savedActualBoard.getBoardIdx()).isEqualTo(savedActualBoard.getBoardIdx());
    }

    @Test
    @DisplayName("게시물을 저장하면 자동으로 생성 날짜가 주입된다.")
    void save_saveBoardWithCreatedDate_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();

        // when
        Board savedTestBoard = boardRepository.save(board);

        // then
        assertThat(savedTestBoard).isNotNull();
        assertThat(savedTestBoard.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("게시물 리스트를 최신순으로 가져온다.")
    public void find_findAllBoardByLatestOrder_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        for (int i = 0; i < 3; i++) {
            Board board = Board.builder()
                    .author(savedTestUser)
                    .boardTitle(boardTitle)
                    .boardContents(boardContents)
                    .boardCategory(boardCategory)
                    .build();

            boardRepository.save(board);
        }

        // when
        List<Board> savedTestBoards = boardRepository.findAllBoardByKeyword("", PageRequest.of(0, 10));

        // then
        assertThat(savedTestBoards).isNotNull();
        assertThat(savedTestBoards.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("검색어로 게시물 리스트를 가져온다.")
    public void find_findAllBoardByKeyword_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        for (int i = 0; i < 3; i++) {
            Board board = Board.builder()
                    .author(savedTestUser)
                    .boardTitle(boardTitle)
                    .boardContents(boardContents)
                    .boardCategory(boardCategory)
                    .build();

            boardRepository.save(board);
        }

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle + keyword)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();

        boardRepository.save(board);

        // when
        List<Board> savedTestBoards = boardRepository.findAllBoardByKeyword(keyword, PageRequest.of(0, 10));

        // then
        assertThat(savedTestBoards).isNotNull();
        assertThat(savedTestBoards.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("오직 활성화 된 게시물만 가져온다.")
    public void find_findBoardByOnlyEnabled_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .boardLocked(true)
                .build();

        boardRepository.save(board);

        // when
        Board savedTestBoard = boardRepository.findEnableBoardByIdx(board.getBoardIdx())
                .orElseThrow(BoardNotFoundException::new);

        // then
        assertThat(savedTestBoard).isNotNull();
        assertThat(savedTestBoard.getBoardLocked()).isTrue();
    }

    @Test
    @DisplayName("비 활성화 된 게시물에 접근하면 예외가 발생한다.")
    public void find_findBoardByOnlyEnabled_Exception() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .boardLocked(false)
                .build();

        // when
        boardRepository.save(board);

        // then
        assertThatThrownBy(() ->
                boardRepository.findEnableBoardByIdx(board.getBoardIdx())
                        .orElseThrow(BoardNotFoundException::new)
        ).isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("활성화, 비 활성화 게시물 무엇이든 가져온다.")
    public void find_findBoardByDisabledAndEnable_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .boardLocked(false)
                .build();

        boardRepository.save(board);

        // when
        Board savedTestBoard = boardRepository.findBoardByIdx(board.getBoardIdx())
                .orElseThrow(BoardNotFoundException::new);

        // then
        assertThat(savedTestBoard).isNotNull();
        assertThat(savedTestBoard.getBoardLocked()).isFalse();
    }

    @Test
    @DisplayName("인덱스로 게시물을 삭제한다.")
    public void delete_deleteBoardByBoardIndex_Success() {
        // given
        User savedTestUser = userRepository.save(
                UserFactory.user()
        );

        Board board = Board.builder()
                .author(savedTestUser)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();

        Long savedBoardIdx = boardRepository.save(board).getBoardIdx();

        // when
        Board savedBoard = boardRepository.findBoardByIdx(savedBoardIdx)
                .orElseThrow(BoardNotFoundException::new);

        assertThat(savedBoard).isNotNull();

        boardRepository.delete(savedBoard);

        // then
        assertThatThrownBy(() ->
                boardRepository.findBoardByIdx(savedBoardIdx)
                        .orElseThrow(BoardNotFoundException::new)
        ).isInstanceOf(BoardNotFoundException.class);
    }

}
