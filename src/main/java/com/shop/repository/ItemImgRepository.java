package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    public abstract List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    public abstract ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn); // 상품의 대표 이미지 찾기

}
