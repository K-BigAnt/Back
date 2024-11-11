package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.config.InterceptorTestConfig;
import com.bigant.gaeme.dao.dto.AuthResponseDto;
import com.bigant.gaeme.repository.enums.AuthCorp;
import com.bigant.gaeme.service.OauthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(OauthController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@AutoConfigureDataJpa
@ExtendWith(RestDocumentationExtension.class)
@Import(InterceptorTestConfig.class)
public class OauthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OauthService oauthService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAuthCodeTest() throws Exception {
        BDDMockito.given(oauthService.getAuthUri(AuthCorp.KAKAO))
                .willReturn("http://localhost/oauth/authorize?client_id=kakao_id&response_type=code&redirect_uri=http://localhost/auth/callback");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/auth/{auth_corp}", "kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(
            "http://localhost/oauth/authorize?client_id=kakao_id&response_type=code&redirect_uri=http://localhost/auth/callback"
                ))
                .andDo(getAuthGetResultHandler())
                .andDo(print());
    }

    private RestDocumentationResultHandler getAuthGetResultHandler() {
        return document("auth/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("auth_corp").description("OAuth 플랫폼 (kakao, naver, google)")
                )
        );
    }

    @Test
    public void signInOrSignUpTest() throws Exception {
        AuthResponseDto result = AuthResponseDto.builder()
                .token("jwt token")
                .isSignIn(true)
                .build();
        BDDMockito.given(oauthService.signInOrSignUp(BDDMockito.any(), BDDMockito.any()))
                .willReturn(result);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/auth/{auth_corp}", "naver")
                        .queryParam("code", "auth_code_blahblah"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(result)))
                .andDo(getSignInOrSignUpPostResultHandler())
                .andDo(print());

        BDDMockito.verify(oauthService).signInOrSignUp("auth_code_blahblah", AuthCorp.NAVER);
    }

    private ResultHandler getSignInOrSignUpPostResultHandler() {
        return document("sign-in-or-sign-up/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("code").description("인가 코드")
                ),
                pathParameters(
                        parameterWithName("auth_corp").description("OAuth 플랫폼 (kakao, naver, google)")
                )
        );
    }


}
