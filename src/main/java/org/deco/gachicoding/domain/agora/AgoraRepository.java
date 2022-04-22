package org.deco.gachicoding.domain.agora;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgoraRepository extends JpaRepository<Agora, Long> {

    Page<Agora> findAllByOrderByAgoraIdxAsc(Pageable pageable);
}
