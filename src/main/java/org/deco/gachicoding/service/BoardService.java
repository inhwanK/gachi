package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    ResponseEntity<ResponseState> registerBoard(BoardSaveRequestDto dto);

    Page<BoardResponseDto> getBoardList(Pageable pageable);

    BoardResponseDto getBoardDetail(Long boardIdx);

    BoardResponseDto modifyBoard(BoardUpdateRequestDto dto);

    ResponseEntity<ResponseState> disableBoard(Long boardIdx);

    ResponseEntity<ResponseState> enableBoard(Long boardIdx);

    ResponseEntity<ResponseState> removeBoard(Long boardIdx);
}
