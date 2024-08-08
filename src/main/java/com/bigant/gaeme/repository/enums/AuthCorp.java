package com.bigant.gaeme.repository.enums;

import lombok.Getter;

@Getter
public enum AuthCorp {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    AuthCorp(String name) {
        this.name = name;
    }

    private final String name;

}
