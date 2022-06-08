package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    public abstract Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
