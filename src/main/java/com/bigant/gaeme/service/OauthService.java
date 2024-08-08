package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.enums.AuthCorp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthClient kakaoOauthClient;

    private final OauthClient naverOauthClient;

    private final OauthClient googleOauthClient;

    public String getAuthUri(String redirectUri, AuthCorp authCorp) {
        return switch (authCorp) {
            case KAKAO -> kakaoOauthClient.getAuthUri(redirectUri).toString();
            case NAVER -> naverOauthClient.getAuthUri(redirectUri).toString();
            case GOOGLE -> googleOauthClient.getAuthUri(redirectUri).toString();
        };
    }

}
