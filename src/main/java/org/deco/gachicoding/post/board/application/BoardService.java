package org.deco.gachicoding.post.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.board.BoardInactiveException;
import org.deco.gachicoding.exception.post.board.BoardNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.post.board.application.dto.BoardDtoAssembler;
import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.repository.BoardRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    @Transactional(rollbackFor = Exception.class)
    public Long registerBoard(BoardSaveRequestDto dto) {

        Board board = boardRepository.save(createBoard(dto));

        // 경로에서 idx 빼버릴까
        Long boardIdx = board.getBoardIdx();
        String boardContent = board.getBoardContents();

        board.updateContent(
                fileService.extractPathAndS3Upload(boardIdx, ArticleType.BOARD, boardContent)
        );

        // tagify 라이브러리
//        if (dto.getTags() != null)
//            tagService.registerBoardTag(boardIdx, dto.getTags(), BOARD);

        return board.getBoardIdx();
    }

    private Board createBoard(BoardSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());

        return BoardDtoAssembler.board(user, dto);
    }

    @Transactional
    public List<BoardResponseDto> getBoardList(BoardListRequestDto dto) {

        return BoardDtoAssembler.boardResponseDtos(
                boardRepository.findAllBoardByKeyword(
                        dto.getKeyword(), dto.getPageable()
                )
        );
    }

    @Transactional
    public BoardResponseDto getBoardDetail(BoardDetailRequestDto dto) {

        Board board = findBoard(dto.getBoardIdx());

        if (!board.getBoardLocked())
            throw new BoardInactiveException();

//        tagService.getTags(boardIdx, BOARD, boardDetail);

        return BoardDtoAssembler.boardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {

        // 무조건 async
        String updateContents = fileService.compareFilePathAndOptimization(
                dto.getBoardIdx(),
                ArticleType.BOARD,
                dto.getBoardContents()
        );

        Board board = findBoard(dto.getBoardIdx());

        if (!board.getBoardLocked())
            throw new BoardInactiveException();

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.updateTitle(dto.getBoardTitle());

        // blocking
        board.updateContent(updateContents);

        return BoardDtoAssembler.boardResponseDto(board);
    }

    @Transactional
    public void disableBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.disableBoard();
    }

    @Transactional
    public void enableBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.enableBoard();
    }

    @Transactional
    public void removeBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        boardRepository.delete(board);
    }

    private Board findBoard(Long boardIdx) {
        return boardRepository.findBoardByIdx(boardIdx)
                .orElseThrow(BoardNotFoundException::new);
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
