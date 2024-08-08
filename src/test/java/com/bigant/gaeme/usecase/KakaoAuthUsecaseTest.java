package com.bigant.gaeme.usecase;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KakaoAuthUsecaseTest {

    private final KakaoAuthUsecase authUsecase;

    private final UserRepository userRepository;

    private final JwtBuilder jwtBuilder;

    private final OauthClient oauthClient;

    private final ObjectMapper objectMapper;

    @Autowired
    KakaoAuthUsecaseTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.oauthClient = Mockito.mock(OauthClient.class);
        this.jwtBuilder = new JwtBuilder("abcdabcdabcdabcdabcdabcdabcdabcd");
        this.objectMapper = new ObjectMapper();
        this.authUsecase = new KakaoAuthUsecase(oauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Test
    void 유저_정보_파싱_성공() {
        //given
        String json = """
            {
                "id": "1234"
            }
        """;

        //when
        String result = authUsecase.exportOauthId(json);

        //then
        Assertions.assertEquals("1234", result);
    }

}
