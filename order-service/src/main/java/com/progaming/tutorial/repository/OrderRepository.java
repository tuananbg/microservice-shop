package com.progaming.tutorial.repository;

import com.progaming.tutorial.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
