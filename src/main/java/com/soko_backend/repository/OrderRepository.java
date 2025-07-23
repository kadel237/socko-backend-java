package com.soko_backend.repository;

import com.soko_backend.entity.order.Order;
import com.soko_backend.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(UserEntity customer);
}
