package org.deco.gachicoding.domain.auth;

import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, String> {

//    Optional<UUID> findByTokenAndExpirationDateAfterAndExpired(String confirmationTokenId, LocalDateTime now, boolean expired);
    Optional<Auth> findByAuthEmailAndAuthExpdateAfterAndExpiredIsFalse(String email, LocalDateTime now);
    Optional<Auth> findByAuthEmail(String email);
}
