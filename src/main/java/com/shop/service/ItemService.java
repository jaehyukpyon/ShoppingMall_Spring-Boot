package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Log
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        log.info("========== ItemService's saveItem ==========");
        log.info("List<MultipartFile> itemImgFileList's size(): " + itemImgFileList.size()); // 실제 관리자가 몇 개의 이미지를 등록하든 size()는 항상 5로 고정.

        // Item 등록
        // ItemImg는 Item에 ManyToOne으로 단방향 mapping 돼 있음. 즉, Item에서는 ItemImg를 참조(매핑)하지 않음
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 상품의 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item); // ItemImg entity에 FK 값 insert

            if (i == 0) { // 첫 번째 이미지의 경우 대표 이미지로 설정
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        // 상품의 PK를 받아서, 상품에 연관된 이미지 엔티티를 찾은 후, DTO로 변환하고 List에 담은 후, 상품 또한 DTO로 변환하고,
        // 그 상품의 List<ItemImgDto>에 DTO로 변환된 상품 이미지 ArrayList를 저장.

        // parameter로 넘어온 Item entity의 itemid를 사용하여 연관 되어 있는 ItemImg 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    // 상품 정보 수정 (영속성 컨텍스트의 변경 감지 사용)
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 상품 이미지 아이디 리스트를 조회

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    // QueryDSL 을 사용하는 Item entity 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        ;
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // main 화면에 보여줄 아이템 정보 갖고오기 (MainItemDto)
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        ;
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}
