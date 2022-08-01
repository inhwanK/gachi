package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.board.BoardRepository;
import org.deco.gachicoding.domain.comment.Comment;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.dto.question.QuestionDetailResponseDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.BoardService;
import org.deco.gachicoding.service.FileService;
import org.deco.gachicoding.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    @Transactional
    @Override
    public Long registerBoard(BoardSaveRequestDto dto, String boardCategory) throws Exception {
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Board board = boardRepository.save(dto.toEntity(writer, boardCategory));

        Long boardIdx = board.getBoardIdx();
        String boardContent = board.getBoardContent();

        if(dto.getTags() != null)
            tagService.registerBoardTag(boardIdx, dto.getTags(), boardCategory);

        try {
            board.updateContent(fileService.extractImgSrc(boardIdx, boardContent, boardCategory));
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Board Content");
            e.printStackTrace();
            removeBoard(boardIdx);
            tagService.removeBoardTags(boardIdx, boardCategory);
            // throw해줘야 Advice에서 예외를 감지 함
            throw e;
        }

        return boardIdx;
    }

    @Transactional
    @Override
    public Page<BoardResponseDto> getBoardList(String keyword, Pageable pageable, String boardCategory) {
        Page<BoardResponseDto> boardList =
                boardRepository.findByBoardCategoryAndBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardCategoryAndBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(boardCategory, keyword, boardCategory, keyword, pageable).map(entity -> new BoardResponseDto(entity));

        boardList.forEach(
                boardResponseDto ->
                        tagService.getTags(boardResponseDto.getBoardIdx(), boardCategory, boardResponseDto)
        );

        return boardList;
    }

    @Transactional
    @Override
    public BoardResponseDto getBoardDetail(Long boardIdx, String boardCategory) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        BoardResponseDto boardDetail = BoardResponseDto.builder()
                .board(board)
                .build();

//        fileService.getFiles(boardIdx, boardCategory, boardDetail);
        tagService.getTags(boardIdx, boardCategory, boardDetail);

        return boardDetail;
    }

    @Override
    @Transactional
    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {
        Board board = boardRepository.findById(dto.getBoardIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!isSameWriter(board, user)) {
            throw new CustomException(INVALID_AUTH_USER);
        }

        board = board.update(dto.getBoardTitle(), dto.getBoardContent());

        BoardResponseDto boardDetail = BoardResponseDto.builder()
                .board(board)
                .build();

        return boardDetail;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        board.disableBoard();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        board.enableBoard();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        boardRepository.delete(board);

        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Board board, User user) {
        String writerEmail = board.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
