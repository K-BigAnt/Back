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
public class NaverAuthUsecaseTest {
    private final NaverAuthUsecase authUsecase;

    private final UserRepository userRepository;

    private final JwtBuilder jwtBuilder;

    private final OauthClient oauthClient;

    private final ObjectMapper objectMapper;

    @Autowired
    NaverAuthUsecaseTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.oauthClient = Mockito.mock(OauthClient.class);
        this.jwtBuilder = new JwtBuilder("abcdabcdabcdabcdabcdabcdabcdabcd");
        this.objectMapper = new ObjectMapper();
        this.authUsecase = new NaverAuthUsecase(oauthClient, userRepository, jwtBuilder, objectMapper);
    }

    @Test
    void 유저정보_파싱_성공() {
        //given
        String json = """
                {
                  "resultcode": "00",
                  "message": "success",
                  "response": {
                    "email": "openapi@naver.com",
                    "nickname": "OpenAPI",
                    "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
                    "age": "40-49",
                    "gender": "F",
                    "id": "32742776",
                    "name": "오픈 API",
                    "birthday": "10-01",
                    "birthyear": "1900",
                    "mobile": "010-0000-0000"
                  }
                }
                """;

        //when
        String result = authUsecase.exportOauthId(json);

        //then
        Assertions.assertEquals("32742776", result);

    }

}
