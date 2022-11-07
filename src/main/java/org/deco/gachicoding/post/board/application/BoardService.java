package org.deco.gachicoding.post.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.board.BoardInactiveException;
import org.deco.gachicoding.exception.post.board.BoardNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
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
import java.util.Queue;

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
                fileService.extractPathAndS3Upload(boardIdx, boardContent, "BOARD")
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
//        Page<BoardResponseDto> boardList =
//                boardRepository.findAllBoardByKeyword(keyword, pageable).map(entity -> new BoardPostResponseDto(entity));

//        boardList.forEach(
//                boardResponseDto ->
//                        tagService.getTags(boardResponseDto.getBoardIdx(), BOARD, boardResponseDto)
//        );

        return BoardDtoAssembler.boardResponseDtos(boardRepository.findAllBoardByKeyword(dto.getKeyword(), dto.getPageable()));
    }

    @Transactional
    public BoardResponseDto getBoardDetail(BoardDetailRequestDto dto) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

//        fileService.getFiles(boardIdx, boardCategory, boardDetail);
//        tagService.getTags(boardIdx, BOARD, boardDetail);

        Board board = findBoard(dto.getBoardIdx());

        if (!board.getBoardLocked())
            throw new BoardInactiveException();

        return BoardDtoAssembler.boardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        if (!board.getBoardLocked())
            throw new BoardInactiveException();

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.updateTitle(dto.getBoardTitle());

        board.updateContent(dto.getBoardContents());

        // 게시물의 내용에서 이미지 경로만 추출
        

        // 해당 게시물에 있는 모든 메타 데이터를 찾는다.


        // 내용안에 이미지 경로와 디비에 저장된 이미지 메타 데이터와 비교 후, 하나 씩 제거


        // queue의 내용물이 남아 있다면, 디비의 메타 데이터 비활성



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
