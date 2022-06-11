package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**", "/wallpaper/**", "/kakao/**") // web browser에 입력하는 ulr에 /images로 시작하는 경우, uploadPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정.
                    .addResourceLocations(uploadPath, "classpath:/static/wallpaper/", "classpath:/static/kakao/");
    }

}
