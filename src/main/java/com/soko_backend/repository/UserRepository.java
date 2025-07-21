package com.soko_backend.repository;

import com.soko_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByResetToken(String resetToken);
    Optional<UserEntity> findOneWithRolesByLoginIgnoreCase(String login);
    boolean existsByEmail(String email);
}
