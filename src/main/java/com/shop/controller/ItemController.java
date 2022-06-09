package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log
@Controller
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());

        model.addAttribute("testValue", "thisIsaTestValue");

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        // @RequestParam("itemImgFile") >> itemForm.html에서, input[type='file'][name='itemImgFile']
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "한 개 이상 상품 이미지를 필수로 등록해야 합니다.");
            return "item/itemForm";
        }

        try {
            Long savedItemId = itemService.saveItem(itemFormDto, itemImgFileList);
            log.info("New Item saved successfully - Primary Key: " + savedItemId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 오류가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    // 상품 수정 form return
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
        }

        return "item/itemForm";
    }

    // 상품 수정 post
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "한 개 이상 상품 이미지를 필수로 등록해야 합니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 정보 수정 중 오류가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    // 상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달하는 부분
    // 한 페이지 당 세 개의 상품만 display
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

        log.info("@@@@@@@@@@ itemSearchDto - searchDateType: " + itemSearchDto.getSearchDateType());
        log.info("@@@@@@@@@@ itemSearchDto - searchBy: " + itemSearchDto.getSearchBy());
        log.info("@@@@@@@@@@ itemSearchDto - searchQuery: " + itemSearchDto.getSearchQuery() + "\r\n");

        // ** pageable의 getOffset은 page * size를 반환.
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // 조회할 페이지 번호 및 한 번에 갖고올 데이터 개수

        log.info("========== ItemController's itemManage - pageable.getOffset(): " + pageable.getOffset());
        log.info("========== ItemController's itemManage - pageable.getPageSize(): " + pageable.getPageSize());

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        // List<Item> itemList = items.getContent();

        log.info("items.number: " + items.getNumber());
        log.info("items.maxPage: " + items.getTotalPages());
        log.info("items.isFirst(): " + items.isFirst()); // isFirst를 사용하여 첫 번째 페이지 여부 확인 가능... 0번부터 시작!!
        log.info("items.isLast(): " + items.isLast());
        /*
        * 상품이 네 개만 존재하고, 상품 관리를 처음 클릭 할 경우... (즉 첫 번째 page)
        * items.number: 0 >> 현재 페이지를 알 수 있다
        * items.maxPage: 2
        * items.isFirst(): true
        * items.isLast(): false
        *
        * 상품이 네 개 존재하고, html에서 두 번째 페이지를 클릭하는 경우
        * items.number: 1
        * items.maxPage: 2
        * items.isFirst(): false
        * items.isLast(): true
        * */

        model.addAttribute("items", items);
        //model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시, 기존 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달.
        model.addAttribute("maxPage", 5); // 페이지 번호의 최대 개수 previous 1, 2, 3, 4, 5 next

        return "item/itemMng";
    }

    // 상품 상세 페이지
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);

        model.addAttribute("item", itemFormDto);

        return "item/itemDtl";
    }

}
