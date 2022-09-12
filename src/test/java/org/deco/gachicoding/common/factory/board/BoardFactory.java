package org.deco.gachicoding.common.factory.board;

import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.user.domain.User;

public class BoardFactory {
    private BoardFactory() {}

    /* Board Object start */
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
}
