package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface BoardService {

    Long registerBoard(BoardSaveRequestDto dto, String boardType) throws Exception;

    Page<BoardResponseDto> getBoardList(String keyword, Pageable pageable, String boardType);

    BoardResponseDto getBoardDetail(Long boardIdx, String boardType);

    BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) throws RuntimeException;

    ResponseEntity<ResponseState> disableBoard(Long boardIdx);

    ResponseEntity<ResponseState> enableBoard(Long boardIdx);

    ResponseEntity<ResponseState> removeBoard(Long boardIdx);
}
