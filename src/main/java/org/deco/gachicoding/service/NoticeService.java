package org.deco.gachicoding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.notice.NoticeRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.notice.NoticeSaveRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    String NOTICE = "NOTICE";

    @Transactional(rollbackFor = Exception.class)
    public Long registerNotice(NoticeSaveRequestDto dto) throws Exception {
        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.

        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Notice notice = noticeRepository.save(dto.toEntity(writer));

        Long notIdx = notice.getNotIdx();
        String notContent = notice.getNotContent();

        try {
            notice.updateContent(fileService.extractImgSrc(notIdx, notContent, NOTICE));
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Notice Content");
            e.printStackTrace();
            // throw해줘야 Advice에서 예외를 감지 함
            throw e;
        }

        return notIdx;
    }

//    @Transactional
//    public Page<BoardResponseDto> getBoardList(String keyword, Pageable pageable) {
//        Page<BoardResponseDto> boardList =
//                boardRepository.findByBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(keyword, keyword, pageable).map(entity -> new BoardResponseDto(entity));
//
//        boardList.forEach(
//                boardResponseDto ->
//                        tagService.getTags(boardResponseDto.getBoardIdx(), BOARD, boardResponseDto)
//        );
//
//        return boardList;
//    }
//
//    @Transactional
//    public BoardResponseDto getBoardDetail(Long boardIdx) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        BoardResponseDto boardDetail = BoardResponseDto.builder()
//                .board(board)
//                .build();
//
////        fileService.getFiles(boardIdx, boardCategory, boardDetail);
//        tagService.getTags(boardIdx, BOARD, boardDetail);
//
//        return boardDetail;
//    }
//
//    @Transactional
//    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {
//        Board board = boardRepository.findById(dto.getBoardIdx())
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        User user = userRepository.findByUserEmail(dto.getUserEmail())
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//
//        if (!isSameWriter(board, user)) {
//            throw new CustomException(INVALID_AUTH_USER);
//        }
//
//        board = board.update(dto.getBoardTitle(), dto.getBoardContent());
//
//        BoardResponseDto boardDetail = BoardResponseDto.builder()
//                .board(board)
//                .build();
//
//        return boardDetail;
//    }
//
//    @Transactional
//    public ResponseEntity<ResponseState> disableBoard(Long boardIdx) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        board.disableBoard();
//
//        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
//    }
//
//    @Transactional
//    public ResponseEntity<ResponseState> enableBoard(Long boardIdx) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        board.enableBoard();
//
//        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
//    }
//
//    @Transactional
//    public ResponseEntity<ResponseState> removeBoard(Long boardIdx) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));
//
//        boardRepository.delete(board);
//
//        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
//    }
//
//    private Boolean isSameWriter(Board board, User user) {
//        String writerEmail = board.getWriter().getUserEmail();
//        String userEmail = user.getUserEmail();
//
//        return (writerEmail.equals(userEmail)) ? true : false;
//    }
}
