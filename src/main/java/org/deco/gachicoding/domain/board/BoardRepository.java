package org.deco.gachicoding.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByBoardCategoryAndBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardCategoryAndBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardType1, String boardContent,String boardType2,  String boardTitle, Pageable pageable);
}
