package com.bigant.gaeme.config.interceptor;

import com.bigant.gaeme.annotation.Authorized;
import com.bigant.gaeme.annotation.Authorized.AuthRole;
import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.repository.UserRepository;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.method.HandlerMethod;

public class AuthInterceptorTest {

    private final JwtBuilder jwtBuilder;

    private final AuthInterceptor authInterceptor;

    private final HandlerMethod handlerMethod;

    private final UserRepository userRepository;

    AuthInterceptorTest() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.handlerMethod = Mockito.mock(HandlerMethod.class);
        this.jwtBuilder = new JwtBuilder("abcdabcdabcdabcdabcdabcdabcdabcd");
        this.authInterceptor = new AuthInterceptor(userRepository, jwtBuilder);
    }

    private Authorized getAuthorized(AuthRole role) {
        return new Authorized() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Authorized.class;
            }

            @Override
            public AuthRole authRole() {
                return role;
            }
        };
    }

    @Test
    void 멤버_인증_성공() throws Exception {
        //given
        BDDMockito.given(userRepository.existsById(1L)).willReturn(true);
        BDDMockito.given(handlerMethod.getMethodAnnotation(BDDMockito.eq(Authorized.class)))
                .willReturn(getAuthorized(AuthRole.MEMBER));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtBuilder.createJwt(1L));
        //when
        boolean result = authInterceptor.preHandle(request, response, handlerMethod);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    void 멤버_인증_실패() {
        //given
        BDDMockito.given(handlerMethod.getMethodAnnotation(BDDMockito.eq(Authorized.class)))
                .willReturn(getAuthorized(AuthRole.MEMBER));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when, then
        Assertions.assertThrows(AuthenticationServiceException.class, () -> {
            authInterceptor.preHandle(request, response, handlerMethod);
        });
    }

}
