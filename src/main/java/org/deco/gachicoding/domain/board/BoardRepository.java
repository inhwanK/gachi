package org.deco.gachicoding.domain.board;

import org.deco.gachicoding.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardContent, String boardTitle, Pageable pageable);
}
