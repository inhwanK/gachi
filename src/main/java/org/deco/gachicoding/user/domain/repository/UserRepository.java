package org.deco.gachicoding.user.domain.repository;

import org.deco.gachicoding.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);

    void deleteByUserEmail(String userEmail);

    Boolean existsByUserEmail(String email);
}