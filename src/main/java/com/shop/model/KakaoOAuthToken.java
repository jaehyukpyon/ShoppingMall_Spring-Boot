package com.shop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoOAuthToken {

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private int expiresIn;

    private String scope;

    private int refreshTokenExpiresIn;

}
