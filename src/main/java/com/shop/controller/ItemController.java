package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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

}
