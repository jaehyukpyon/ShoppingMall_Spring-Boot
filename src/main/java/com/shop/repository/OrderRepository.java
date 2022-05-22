package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*
    * Order 엔티티를 저장하기 위한 인터페이스
    * */

}
