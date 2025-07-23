package com.soko_backend.repository.cart;


import com.soko_backend.entity.cart.Cart;
import com.soko_backend.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(UserEntity user);
}