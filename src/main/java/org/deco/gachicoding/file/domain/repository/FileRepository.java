package org.deco.gachicoding.file.domain.repository;

import org.deco.gachicoding.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f.filePath FROM File f WHERE f.articleCategory = :category AND f.articleIdx = :idx")
    List<String> findFilePathByCategoryAndIdx(@Param("category") String category, @Param("idx") Long idx);
}
