package com.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }

    @ResponseBody
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
    }

}
