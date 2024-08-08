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

import com.bigant.gaeme.repository.enums.AuthCorp;
import com.bigant.gaeme.service.AuthResponseDto;
import com.bigant.gaeme.service.OauthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(OauthController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
public class OauthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OauthService oauthService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAuthCodeTest() throws Exception {
        BDDMockito.given(oauthService.getAuthUri("http://localhost/auth/callback", AuthCorp.KAKAO))
                .willReturn("http://localhost/oauth/authorize?client_id=kakao_id&response_type=code&redirect_uri=http://localhost/auth/callback");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/auth/{auth_corp}", "kakao")
                        .param("redirect_uri", "http://localhost/auth/callback"))
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
                ),
                queryParameters(
                        parameterWithName("redirect_uri").description("인가코드를 받을 리다이렉트 URI")
                )
        );
    }

    @Test
    public void signInOrSignUpTest() throws Exception {
        AuthResponseDto result = AuthResponseDto.builder()
                .token("jwt token")
                .isSignIn(true)
                .build();
        BDDMockito.given(oauthService.signInOrSignUp(BDDMockito.any(), BDDMockito.any(), BDDMockito.any()))
                .willReturn(result);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/auth/{auth_corp}", "naver")
                        .queryParam("code", "auth_code_blahblah")
                        .queryParam("redirect_uri", "http://localhost/auth/callback"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(result)))
                .andDo(getSignInOrSignUpPostResultHandler())
                .andDo(print());

        BDDMockito.verify(oauthService).signInOrSignUp("http://localhost/auth/callback", "auth_code_blahblah", AuthCorp.NAVER);
    }

    private ResultHandler getSignInOrSignUpPostResultHandler() {
        return document("sign-in-or-sign-up/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("code").description("인가 코드"),
                        parameterWithName("redirect_uri").description("인가코드를 받은 리다이렉트 URI")
                ),
                pathParameters(
                        parameterWithName("auth_corp").description("OAuth 플랫폼 (kakao, naver, google)")
                )
        );
    }


}
