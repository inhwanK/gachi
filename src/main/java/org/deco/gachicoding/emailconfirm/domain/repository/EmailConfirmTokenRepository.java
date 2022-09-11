package org.deco.gachicoding.emailconfirm.domain.repository;


import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmToken, String> {

    @Query("select mct " +
            "from EmailConfirmToken mct " +
            "where mct.targetEmail = :targetEmail " +
                "and mct.expiredAt > current_time ")
    Optional<EmailConfirmToken> retrieveValidToken(String targetEmail);

}
