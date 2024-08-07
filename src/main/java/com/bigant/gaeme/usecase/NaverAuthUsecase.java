package com.bigant.gaeme.usecase;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class NaverAuthUsecase extends AuthUsecase {

    public NaverAuthUsecase(OauthClient naverOauthClient, UserRepository userRepository, JwtBuilder jwtBuilder, ObjectMapper objectMapper) {
        super(naverOauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Override
    protected String exportOauthId(String json) {
        String dataJson = exportJsonField(json, "response");

        return exportJsonField(dataJson, "id");
    }

}
