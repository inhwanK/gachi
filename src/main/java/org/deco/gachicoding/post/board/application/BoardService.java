package org.deco.gachicoding.post.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.repository.BoardRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.board.dto.response.BoardPostResponseDto;
import org.deco.gachicoding.post.board.dto.request.BoardSaveRequestDto;
import org.deco.gachicoding.post.board.dto.request.BoardUpdateRequestDto;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.exception.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    String BOARD = "BOARD";

    @Transactional
    public Long registerBoard(BoardSaveRequestDto dto) throws Exception {
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.save(dto.toEntity(writer));

        Long boardIdx = board.getBoardIdx();
        String boardContent = board.getBoardContents();

        if (dto.getTags() != null)
            tagService.registerBoardTag(boardIdx, dto.getTags(), BOARD);

        try {
            board.updateContent(fileService.extractImgSrc(boardIdx, boardContent, BOARD));
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Board Content");
            e.printStackTrace();
            removeBoard(boardIdx);
            tagService.removeBoardTags(boardIdx, BOARD);
            // throw해줘야 Advice에서 예외를 감지 함
            throw e;
        }

        return boardIdx;
    }

    @Transactional
    public Page<BoardPostResponseDto> getBoardList(String keyword, Pageable pageable) {
        Page<BoardPostResponseDto> boardList =
                boardRepository.findByBoardContentsContainingIgnoreCaseAndBoardActivatedTrueOrBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(keyword, keyword, pageable).map(entity -> new BoardPostResponseDto(entity));

        boardList.forEach(
                boardResponseDto ->
                        tagService.getTags(boardResponseDto.getBoardIdx(), BOARD, boardResponseDto)
        );

        return boardList;
    }

    @Transactional
    public BoardPostResponseDto getBoardDetail(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        BoardPostResponseDto boardDetail = BoardPostResponseDto.builder()
                .board(board)
                .build();

//        fileService.getFiles(boardIdx, boardCategory, boardDetail);
        tagService.getTags(boardIdx, BOARD, boardDetail);

        return boardDetail;
    }

    @Transactional
    public BoardPostResponseDto modifyBoard(BoardUpdateRequestDto dto) {
        Board board = boardRepository.findById(dto.getBoardIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!isSameWriter(board, user)) {
            throw new ApplicationException(INVALID_AUTH_USER);
        }

        board = board.update(dto.getBoardTitle(), dto.getBoardContent());

        BoardPostResponseDto boardDetail = BoardPostResponseDto.builder()
                .board(board)
                .build();

        return boardDetail;
    }

    @Transactional
    public ResponseEntity<ResponseState> disableBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        board.disableBoard();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> enableBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        board.enableBoard();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> removeBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        boardRepository.delete(board);

        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Board board, User user) {
        String writerEmail = board.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
