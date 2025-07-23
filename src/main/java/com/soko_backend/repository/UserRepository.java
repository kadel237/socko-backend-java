package com.soko_backend.repository;

import com.soko_backend.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByResetToken(String resetToken);
    Optional<UserEntity> findOneWithRolesByLoginIgnoreCase(String login);
    boolean existsByEmail(String email);
}
