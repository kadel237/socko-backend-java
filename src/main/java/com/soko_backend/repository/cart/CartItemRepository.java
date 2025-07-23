package com.soko_backend.repository.cart;

import com.soko_backend.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
