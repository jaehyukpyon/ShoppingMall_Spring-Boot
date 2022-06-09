package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /*
    * 한 번의 주문에 여러 개의 상품 주문 가능.
    * OrderItem 엔티티와 Order 엔티티를 다 대 일 단방향 메핑*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    // 주문할 상품과 수량을 받아서 객체 생성
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);

        return orderItem;
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }

    /*private LocalDateTime regTime;

    private LocalDateTime updateTime;*/

}
