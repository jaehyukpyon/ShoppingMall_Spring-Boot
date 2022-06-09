package com.shop.exception;

public class OutOfStockException extends RuntimeException {
    // 상품의 주문 수량이, 현재 재고보다 많을 경우 발생 될 예외 클래스

    public OutOfStockException(String message) {
        super(message);
    }

}
