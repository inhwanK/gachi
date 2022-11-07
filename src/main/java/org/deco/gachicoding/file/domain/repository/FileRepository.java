package org.deco.gachicoding.file.domain.repository;

import org.deco.gachicoding.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.articleCategory = :category AND f.articleIdx = :idx")
    List<File> findFileByCategoryAndIdx(@Param("category") String category, @Param("idx") Long idx);

    @Query("SELECT f FROM File f WHERE f.filePath = :path")
    Optional<File> findFileByFilePath(@Param("path") String path);
}
