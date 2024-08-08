package com.bigant.gaeme.usecase;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.User;
import com.bigant.gaeme.service.AuthResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class AuthUsecaseTest {

    private final KakaoAuthUsecase authUsecase;

    private final UserRepository userRepository;

    private final JwtBuilder jwtBuilder;

    private final OauthClient oauthClient;

    private final ObjectMapper objectMapper;

    @Autowired
    AuthUsecaseTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.oauthClient = Mockito.mock(OauthClient.class);
        this.jwtBuilder = new JwtBuilder("abcdabcdabcdabcdabcdabcdabcdabcd");
        this.objectMapper = new ObjectMapper();
        this.authUsecase = new KakaoAuthUsecase(oauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Test
    void 회원가입_성공() {
        //given
        BDDMockito.given(oauthClient.getAccessToken(BDDMockito.any(), BDDMockito.any())).willReturn("token");
        BDDMockito.given(oauthClient.getUserInfo(Mockito.any())).willReturn("""
            {
                "id": "abcd"
            }
            """);

        //when
        AuthResponseDto result = authUsecase.signInOrSignUp("redirectUri", "authCode");
        User user = userRepository.findByOauthId("abcd").get();

        //then
        Assertions.assertEquals(
                AuthResponseDto.builder()
                        .token(jwtBuilder.createJwt(user.getId()))
                        .isSignIn(true)
                        .build(),
                result
        );
    }

    @Test
    void 로그인_성공() {
        //given
        User user = userRepository.save(User.builder()
                .oauthId("abcd")
                .nickname("nick")
                .build());
        BDDMockito.given(oauthClient.getAccessToken(BDDMockito.any(), BDDMockito.any())).willReturn("token");
        BDDMockito.given(oauthClient.getUserInfo(Mockito.any())).willReturn("""
            {
                "id": "abcd"
            }
            """);

        //when
        AuthResponseDto result = authUsecase.signInOrSignUp("redirectUri", "authCode");

        //then
        Assertions.assertEquals(
                AuthResponseDto.builder()
                        .token(jwtBuilder.createJwt(user.getId()))
                        .isSignIn(false)
                        .build(),
                result
        );
    }

}
