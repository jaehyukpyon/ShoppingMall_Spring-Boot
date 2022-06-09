package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "item")
@Entity
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String itemNm;

    @Column(name = "price", nullable = false)
    private int price;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Column(nullable = false)
    private int stockNumber;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    // 상품 업데이트
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.itemDetail = itemFormDto.getItemDetail();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 매개변수로 넘어온 수량을 현재 재고 수량에서 뺀 후, 0 미만일 경우 OutOfStockException 발생시키기
    public void removeStock(int minusNumber) {
        int restStock = this.stockNumber - minusNumber;

        if (restStock < 0) {
            throw new OutOfStockException("[상품 이름: " + this.itemNm + " >> 재고 부족, 현재 재고 수량: " + this.stockNumber + "]");
        } else {
            this.stockNumber = restStock;
        }
    }

    /*private LocalDateTime regTime;

    private LocalDateTime updateTime;*/

}
