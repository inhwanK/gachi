package org.deco.gachicoding.common.factory.post.board;

import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardSaveRequest;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardUpdateRequest;
import org.deco.gachicoding.post.board.presentation.dto.response.BoardResponse;
import org.deco.gachicoding.user.domain.User;
import org.springframework.data.domain.Pageable;

public class MockBoardFactory {
    private MockBoardFactory() {}

    /* Board Object Start */
    public static Board mockBoard(
            Long boardIdx,
            User author,
            Boolean boardLocked
    ) {
        return MockBoard.builder()
                .boardIdx(boardIdx)
                .author(author)
                .boardLocked(boardLocked)
                .build();
    }

    public static Board mockBoard(
            Long boardIdx,
            User author,
            String boardTitle,
            String boardContents,
            String boardCategory,
            Boolean boardLocked
    ) {
        return MockBoard.builder()
                .boardIdx(boardIdx)
                .author(author)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .boardLocked(boardLocked)
                .build();
    }
    /* Board Object End */

    /* Board Dto Start */
    public static BoardSaveRequestDto mockBoardSaveRequestDto(
            String userEmail,
            String boardTitle,
            String boardContents,
            String boardCategory
    ) {
        return BoardSaveRequestDto.builder()
                .userEmail(userEmail)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();
    }

    public static BoardListRequestDto mockBoardListRequestDto(
            String keyword,
            Pageable pageable
    ) {
        return BoardListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }

    public static BoardDetailRequestDto mockBoardDetailRequestDto(
            Long boardIdx
    ){
        return BoardDetailRequestDto.builder()
                .boardIdx(boardIdx)
                .build();
    }

    public static BoardUpdateRequestDto mockBoardUpdateRequestDto(
        String userEmail,
        Long boardIdx,
        String boardTitle,
        String boardContents
    ) {
        return BoardUpdateRequestDto.builder()
                .userEmail(userEmail)
                .boardIdx(boardIdx)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .build();
    }

    public static BoardBasicRequestDto mockBoardBasicRequestDto(
        String userEmail,
        Long boardIdx
    ) {
        return BoardBasicRequestDto.builder()
                .userEmail(userEmail)
                .boardIdx(boardIdx)
                .build();
    }

    public static BoardResponseDto mockBoardResponseDto(
            Long boardIdx,
            User author,
            Boolean boardLocked
    ) {
        return BoardResponseDto.builder()
                .boardIdx(boardIdx)
                .author(author)
                .boardLocked(boardLocked)
                .build();
    }
    /* Board Dto End */

    /* Board Request Start */
    public static BoardSaveRequest mockBoardSaveRequest(
        String userEmail,
        String boardTitle,
        String boardContents,
        String boardCategory
    ) {
        return BoardSaveRequest.builder()
                .userEmail(userEmail)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardCategory(boardCategory)
                .build();
    }

    public static BoardResponse mockBoardResponse(
            BoardResponseDto dto
    ) {
        return BoardResponse.builder()
                .boardIdx(dto.getBoardIdx())
                .authorNick(dto.getAuthor().getUserNick())
                .authorEmail(dto.getAuthor().getUserEmail())
                .boardTitle(dto.getBoardTitle())
                .boardContents(dto.getBoardContents())
                .boardCategory(dto.getBoardCategory())
                .boardViews(dto.getBoardViews())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static BoardUpdateRequest mockBoardUpdateRequest(
            String userEmail,
            Long boardIdx,
            String boardTitle,
            String boardContents
    ) {
        return BoardUpdateRequest.builder()
                .userEmail(userEmail)
                .boardIdx(boardIdx)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .build();
    }
    /* Board Request End */
}
