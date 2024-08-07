package com.bigant.gaeme.usecase;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.User;
import com.bigant.gaeme.service.AuthResponseDto;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AuthUsecase {

    private final OauthClient oauthClient;

    private final UserRepository userRepository;

    private final JwtBuilder jwtBuilder;

    private final ObjectMapper objectMapper;

    public final String getAuthUri(String redirectUri) {
        return oauthClient.getAuthUri(redirectUri).toString();
    }

    public final AuthResponseDto signInOrSignUp(String redirectUri, String authCode) {
        String accessToken = oauthClient.getAccessToken(authCode, redirectUri);
        String rawUserInfoJson = oauthClient.getUserInfo(accessToken);
        String oauthId = exportOauthId(rawUserInfoJson);
        Optional<User> user = userRepository.findByOauthId(oauthId);

        if (user.isEmpty()) {
            User newUser = userRepository.save(User.builder()
                    .oauthId(oauthId)
                    .build());
            return AuthResponseDto.builder()
                    .isSignIn(true)
                    .token(jwtBuilder.createJwt(newUser.getId()))
                    .build();
        }

        return AuthResponseDto.builder()
                .isSignIn(user.get().getNickname() == null)
                .token(jwtBuilder.createJwt(user.get().getId()))
                .build();
    }

    protected abstract String exportOauthId(String json);

    protected final String exportJsonField(String json, String fieldName) {
        try {
            final JsonNode node = objectMapper.readTree(json);

            if (!node.has(fieldName)) {
                throw new IllegalStateException("잘못된 데이터 형식입니다.");
            }

            return node.get(fieldName).toString();
        } catch (JacksonException e) {
            throw new IllegalStateException("잘못된 데이터 형식입니다.");
        }
    }

}
