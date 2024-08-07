package com.bigant.gaeme.service;

import com.bigant.gaeme.repository.enums.AuthCorp;
import com.bigant.gaeme.usecase.GoogleAuthUsecase;
import com.bigant.gaeme.usecase.KakaoAuthUsecase;
import com.bigant.gaeme.usecase.NaverAuthUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final KakaoAuthUsecase kakaoAuthUsecase;

    private final NaverAuthUsecase naverAuthUsecase;

    private final GoogleAuthUsecase googleAuthUsecase;

    public String getAuthUri(String redirectUri, AuthCorp authCorp) {
        return switch (authCorp) {
            case KAKAO -> kakaoAuthUsecase.getAuthUri(redirectUri);
            case NAVER -> naverAuthUsecase.getAuthUri(redirectUri);
            case GOOGLE -> googleAuthUsecase.getAuthUri(redirectUri);
        };
    }

    public AuthResponseDto signInOrSignUp(String redirectUri, String authCode, AuthCorp authCorp) {
        return switch (authCorp) {
            case KAKAO -> kakaoAuthUsecase.signInOrSignUp(redirectUri, authCode);
            case NAVER -> naverAuthUsecase.signInOrSignUp(redirectUri, authCode);
            case GOOGLE -> googleAuthUsecase.signInOrSignUp(redirectUri, authCode);
        };
    }

}
