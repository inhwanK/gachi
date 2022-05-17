package org.deco.gachicoding.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByBoardCategoryAndBoardIdx(String boardCategory, Long boardIdx);
}
