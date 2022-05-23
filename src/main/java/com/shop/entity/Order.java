package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    /*
    * 주문 상품 엔티티와 일 대 다 매핑
    * FK가 OrderItem 테이블에 있으므로, 연관 관계의 주인은 OrderItem 엔티티.
    * Order 엔티티가 주인이 아니므로, "mappedBy" 속성으로 연관 관계의 주인을 설정
    *
    * 하나의 주문이 여러 개의 주문 상품을 갖으므로, List를 사용하여 매핑함.
    *
    * 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하기 위해 CascadeType.ALL 지정*/
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
