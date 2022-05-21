package org.deco.gachicoding.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

// ID 2개 or ID 0개로 해야함
public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
}
