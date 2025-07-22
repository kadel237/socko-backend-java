package com.soko_backend.repository;

import com.soko_backend.entity.ShopEntity;
import com.soko_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    List<ShopEntity> findAllByOwner(UserEntity owner);

    Optional<ShopEntity> findByIdAndOwner(Long id, UserEntity owner);

    Optional<ShopEntity> findByName(String name);
}