package com.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redirect")
public class RedirectController {

    @GetMapping(value = "/first")
    public String redirectFirst() {
        ;
        return "redirectFirst";
    }

    @GetMapping(value = "/second")
    public String redirectSecond() {
        ;
        return "redirectSecond";
    }

}
