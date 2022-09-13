package org.deco.gachicoding.emailconfirm.domain.repository;


import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmToken, String> {

    Optional<EmailConfirmToken> findByTokenId(UUID tokenId);

    @Query("select count(ect) > 0 " +
            "from EmailConfirmToken ect " +
            "where ect.targetEmail = :targetEmail and ect.expiredAt > current_time ")
    boolean existsByTargetEmail(@Param("targetEmail") String targetEmail);

    @Modifying(clearAutomatically = true)
    @Query("delete " +
            "from EmailConfirmToken ect " +
            "where ect.targetEmail = :targetEmail")
    void deleteByTargetEmail(@Param("targetEmail") String targetEmail);
}
