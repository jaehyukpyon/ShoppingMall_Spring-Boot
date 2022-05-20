package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping(value = "testHTML")
    public String testHTML() {
        return "test/testHTML";
    }

    @GetMapping(value = "notlogined")
    public String notlogined() {
        return "test/notlogined";
    }

}
