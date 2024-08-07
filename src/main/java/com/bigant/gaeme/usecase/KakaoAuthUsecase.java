package com.bigant.gaeme.usecase;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthUsecase extends AuthUsecase {

    @Autowired
    public KakaoAuthUsecase(OauthClient kakaoOauthClient, UserRepository userRepository, JwtBuilder jwtBuilder, ObjectMapper objectMapper) {
        super(kakaoOauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Override
    protected String exportOauthId(String json) {
        return exportJsonField(json, "id");
    }

}
