package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.dto.AuthResponseDto;
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

    public String getAuthUri(AuthCorp authCorp) {
        return switch (authCorp) {
            case KAKAO -> kakaoAuthUsecase.getAuthUri();
            case NAVER -> naverAuthUsecase.getAuthUri();
            case GOOGLE -> googleAuthUsecase.getAuthUri();
        };
    }

    public AuthResponseDto signInOrSignUp(String authCode, AuthCorp authCorp) {
        return switch (authCorp) {
            case KAKAO -> kakaoAuthUsecase.signInOrSignUp(authCode);
            case NAVER -> naverAuthUsecase.signInOrSignUp(authCode);
            case GOOGLE -> googleAuthUsecase.signInOrSignUp(authCode);
        };
    }

}
