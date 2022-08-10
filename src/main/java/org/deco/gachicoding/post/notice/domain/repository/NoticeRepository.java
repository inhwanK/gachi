package org.deco.gachicoding.post.notice.domain.repository;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByNotIdxAndNotActivatedTrue(Long notIdx);

    @Query("SELECT DISTINCT n FROM Notice n LEFT JOIN FETCH n.writer")
    List<Notice> findAllNoticeByKeyword(@Param("keyword") String keyword, Pageable pageable);
}