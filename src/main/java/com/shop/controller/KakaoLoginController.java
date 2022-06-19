package com.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.model.KakaoOAuthToken;
import com.shop.model.KakaoProfile;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/kakao/")
@Log
@RequiredArgsConstructor
public class KakaoLoginController {

    @Value("${kakaoPassword}")
    private String kakaoPassword;

    // private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    @GetMapping(value = "/auth/kakao/callback")
    public String kakaoCallbackUri(@RequestParam("code") String code, HttpServletRequest httpServletRequest) {
        // 1
        log.info("Kakao Login Callback Uri Code: " + code);

        // 2
        ResponseEntity<String> responsedAccessToken = getAccessTokenFromKakao(code);

        // 3 JSON data (included access_token) conversion to Java Object
        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        KakaoOAuthToken kakaoOAuthToken = null;
        String accessToken = null;
        try {
            kakaoOAuthToken = objectMapper1.readValue(responsedAccessToken.getBody(), KakaoOAuthToken.class);
            accessToken = kakaoOAuthToken.getAccessToken();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 4
        ResponseEntity<String> userProfile = getKakaoUserProfileUsingAccessToken(accessToken);

        // 5 JSON data (included user profile info) conversion to Java Object
        ObjectMapper objectMapper2 = new ObjectMapper();
        objectMapper2.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(userProfile.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("Kakao id(unique): " + kakaoProfile.getId());
        log.info("Kakao email: " + kakaoProfile.getKakaoAccount().getEmail());
        log.info("Kakao nickname: " + kakaoProfile.getKakaoAccount().getProfile().getNickname());
        log.info("Kakao profile image url: " + kakaoProfile.getKakaoAccount().getProfile().getProfileImageUrl());

        String kakaoEmail = kakaoProfile.getKakaoAccount().getEmail();
        String kakaoNickname = kakaoProfile.getKakaoAccount().getProfile().getNickname();

        kakaoUserRegistration(kakaoEmail, kakaoNickname);

        kakaoUserLoginAuthenticationProcess(kakaoEmail, httpServletRequest);

        return "redirect:/redirect/second"; // 카카오로 로그인 성공했다면 여기 적힌 redirect url 로 이동된다.
    }

    public ResponseEntity<String> getAccessTokenFromKakao(String code) {
        // POST request for get AccessToken
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "1def13369a1d9706d362f01b58dbdf62");
        params.add("redirect_uri", "http://localhost:9090/kakao/auth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(
                                                                        params,
                                                                        httpHeaders
                                                                    );

        ResponseEntity<String> response = restTemplate.exchange(
                                            "https://kauth.kakao.com/oauth/token",
                                                HttpMethod.POST,
                                                kakaoTokenRequest,
                                                String.class
                                            );

        log.info("Access Token from Kakao: " + response.getBody());

        return response;
    }

    public ResponseEntity<String> getKakaoUserProfileUsingAccessToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> userProfileRequest = new HttpEntity<>(
                                                                           httpHeaders
                                                                        );

        ResponseEntity<String> response = restTemplate.exchange(
                                                "https://kapi.kakao.com/v2/user/me",
                                                HttpMethod.POST,
                                                userProfileRequest,
                                                String.class
                                            );

        log.info("User Profile from Kakao: " + response.getBody());

        return response;
    }

    public void kakaoUserRegistration(String kakaoEmail, String kakaoNickname) {
        boolean duplicateMember = memberService.isDuplicateMemberForKakaoRegistration(kakaoEmail);

        if (!duplicateMember) {
            log.info("카카오 계정이 DB에 저장 되어 있지 않음. 회원가입 시작...");
            Member member = new Member();
            member.setEmail(kakaoEmail);
            member.setName(kakaoNickname);
            member.setPassword(kakaoPassword);
            member.setRole(Role.USER);

            memberService.saveMemberForKakaoRegistration(member);
        } else {
            log.info("이 이메일로 이미 가입된 카카오 유저 존재 >> email: " + kakaoEmail);
        }
    }

    public void kakaoUserLoginAuthenticationProcess(String kakaoEmail, HttpServletRequest httpServletRequest) {
        log.info("kakaoEmail로 회원 인증...");
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(kakaoEmail, kakaoPassword);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
    }

}
