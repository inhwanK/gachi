package org.deco.gachicoding.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, String> {

//    Optional<ConfirmToken> findByAuthEmailAndAuthExpdateAfterAndExpiredIsFalse(String email, LocalDateTime now);
//    Optional<ConfirmToken> findByAuthToken(UUID authToken);


//    @Query(value = "select ct from ConfirmToken ct " +
//            "where ct.targetEmail = :targetEmail and ct.expired = false")

    default boolean existsConfirmToken(String targetEmail) {
        return existsByTargetEmailAndExpiredIsFalse(targetEmail);
    }

    boolean existsByTargetEmailAndExpiredIsFalse(@Param(value = "targetEmail") String targetEmail);
}
