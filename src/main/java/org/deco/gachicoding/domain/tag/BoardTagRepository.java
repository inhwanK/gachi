package org.deco.gachicoding.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ID 2개 or ID 0개로 해야함
public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    List<BoardTag> findAllByBoardIdxAndBoardType(Long boardIdx, String type);
}
