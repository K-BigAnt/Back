package com.bigant.gaeme.config;

import com.bigant.gaeme.config.interceptor.AuthInterceptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class InterceptorTestConfig {

    @Bean
    public AuthInterceptor authInterceptor() throws Exception {
        AuthInterceptor mock = Mockito.mock(AuthInterceptor.class);

        BDDMockito.given(mock.preHandle(BDDMockito.any(), BDDMockito.any(), BDDMockito.any())).willReturn(true);
        return mock;
    }

}
