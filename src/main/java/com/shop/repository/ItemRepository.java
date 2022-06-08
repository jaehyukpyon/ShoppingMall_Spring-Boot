package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    public abstract List<Item> findByItemNm(String itemNm);

    public abstract List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    public abstract List<Item> findByPriceLessThan(Integer price);

    public abstract List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    public abstract List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
