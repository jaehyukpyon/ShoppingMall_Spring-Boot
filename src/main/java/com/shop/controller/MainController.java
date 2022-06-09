package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Controller
@Log
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        log.info("========== MainController's main() starts ==========");

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        log.info("========== MainController's main() ends ==========\r\n");
        return "main";
    }

    @GetMapping(value = "testImage")
    public void testImage() {
        ;
    }




    /*@ResponseBody
    @GetMapping("/ajax/request1")
    public ResponseEntity<String> request1(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        System.out.println(httpServletRequest.getHeader("Host"));
        System.out.println((String) httpServletRequest.getHeader("Referer"));
        System.out.println("_csrf: " + httpServletRequest.getHeader("_csrf"));
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if ("CSRF_TOKEN".equals(cookie.getName())) {
                System.out.println("CSRF_TOKEN: " + URLDecoder.decode(cookie.getValue(), "UTF-8"));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("ajax success!");
    }

    @GetMapping("/ajax/request1HTML")
    public String request1HTML(HttpServletRequest httpServletRequest) {
        return "ajax/request1HTML";
    }

    @GetMapping("/xsstest")
    public String xsstest(String param1) {
        return "xsstest/xsstest1";
    }*/

}
