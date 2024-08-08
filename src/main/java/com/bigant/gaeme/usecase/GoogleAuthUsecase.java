package com.bigant.gaeme.usecase;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthUsecase extends AuthUsecase {

    public GoogleAuthUsecase(OauthClient googleOauthClient, UserRepository userRepository, JwtBuilder jwtBuilder, ObjectMapper objectMapper) {
        super(googleOauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Override
    protected String exportOauthId(String json) {
        String idWithDoubleQuote = exportJsonField(json, "sub");

        return idWithDoubleQuote.substring(1, idWithDoubleQuote.length() - 1);
    }

}
