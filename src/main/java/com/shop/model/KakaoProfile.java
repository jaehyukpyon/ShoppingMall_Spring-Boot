package com.shop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {

    public Long id;
    public String connectedAt;
    public Properties properties;
    public KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class Properties {

        public String nickname;
        public String profileImage;
        public String thumbnailImage;
    }

    @Getter
    @Setter
    public static class KakaoAccount {

        public Boolean profileNicknameNeedsAgreement;
        public Boolean profileImageNeedsAgreement;
        public Profile profile;
        public Boolean hasEmail;
        public Boolean emailNeedsAgreement;
        public Boolean isEmailValid;
        public Boolean isEmailVerified;
        public String email;

        @Getter
        @Setter
        public static class Profile {

            public String nickname;
            public String thumbnailImageUrl;
            public String profileImageUrl;
            public Boolean isDefaultImage;
        }
    }

}
