package org.deco.gachicoding.service.board;

import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    Page<BoardResponseDto> getBoardList(Pageable pageable);

    BoardResponseDto getBoardDetail(Long boardIdx);

    Long registerBoard(BoardSaveRequestDto dto);

    Long removeBoard(Long boardIdx);
}
