package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        // 상품 판매 상태 조건이 전체 즉 (null)일 경우에는 null을 return.
        // 결과 값이 null일 경우, where절에서 해당 조건은 무시하게 됨.
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> results = queryFactory.selectFrom(QItem.item) // 상품 데이터를 조회하기 위해 QItem의 item을 지정
                                                    .where( // BooleanExpression을 반환하는 조건문을 넣어줌. Comma로 구분되게 넣어줄 경우 AND 조건으로 연산
                                                            regDtsAfter(itemSearchDto.getSearchDateType()),
                                                            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                                                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                                                          )
                                                    .orderBy(QItem.item.id.desc())
                                                    .offset(pageable.getOffset()) // 데이터를 갖고 올 시작 인덱스 지정
                                                    .limit(pageable.getPageSize()) // 한 번에 갖고 올 최대 개수 지정 >> 즉 몇 번째 row에서 부터(offset) 몇 개의 row를 갖고 올 건지(limit)
                                                    .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
