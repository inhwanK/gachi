package org.deco.gachicoding.post.board.domain.repository;

import org.deco.gachicoding.post.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    Page<Board> findByBoardCategoryAndBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardCategoryAndBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardType1, String boardContent,String boardType2,  String boardTitle, Pageable pageable);
    Page<Board> findByBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardContent, String boardTitle, Pageable pageable);
}
