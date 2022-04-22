package org.deco.gachicoding.service.board.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.board.BoardRepository;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardSaveRequestDto;
import org.deco.gachicoding.service.board.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다. boardIdx = " + boardIdx));

        return new BoardResponseDto(entity);
    }

    @Transactional
    @Override
    public Long registerBoard(BoardSaveRequestDto dto) {

        return boardRepository.save(dto.toEntity()).getBoardIdx();
    }

    @Transactional
    @Override
    public Long removeBoard(Long boardIdx) {
        boardRepository.deleteById(boardIdx);
        return boardIdx;
    }
}
