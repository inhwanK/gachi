package org.deco.gachicoding.service.board.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.board.BoardRepository;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.service.board.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public ResponseEntity<ResponseState> registerBoard(BoardSaveRequestDto dto) {
        boardRepository.save(dto.toEntity());

        return ResponseState.toResponseEntity(SAVE_SUCCESS);
    }

    @Transactional
    @Override
    public Page<BoardResponseDto> getBoardList(Pageable pageable) {
        Page<BoardResponseDto> boardList =
                boardRepository.findAllByOrderByBoardIdxAsc(pageable).map(entity -> new BoardResponseDto(entity));

        return boardList;
    }

    @Transactional
    @Override
    public BoardResponseDto getBoardDetail(Long boardIdx) {
        Board entity = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        return new BoardResponseDto(entity);
    }

    @Override
    @Transactional
    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {
        Long boardIdx = dto.getBoardIdx();
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

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
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        board.disableBoard();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        board.enableBoard();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeBoard(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        boardRepository.delete(board);

        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }
}
