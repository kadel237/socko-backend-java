package com.soko_backend.repository.wishlist;

import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.entity.wishList.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(UserEntity user);
}