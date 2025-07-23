package com.soko_backend.repository.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<com.soko_backend.entity.wishlist.WishlistItem, Long> {
}
