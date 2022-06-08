package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    /*
    * 현재 시간과 상품 등록일을 비교하여 상품 조회. 조회 시간 기준은:
    * all: 상품 등록일 전체
    * 1d: 최근 하루 동안 등록
    * 1w: 최근 일주일 동안 등록
    * 1m: 최근 한 달 동안 등록
    * 6m: 최근 6개월 동안 등록 */
    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    /*
    * 상품을 조회할 때 어떤 유형으로 조회할지 선택
    * itemNm: 상품 명
    * createdBy: 상품 등록자의 아이디 */
    private String searchBy;

    /*
    * 조회할 검색어를 저장할 변수.
    * 즉, searcyBy가 itemNm일 경우 상품명을 기준으로 검색
    * createdBy일 경우 상품 등록자 아이디를 기준으로 검색 */
    private String searchQuery = "";

}
