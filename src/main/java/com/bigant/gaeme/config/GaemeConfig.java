package com.bigant.gaeme.config;

import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.usecase.StockDataUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GaemeConfig {

    private final UsStockRepository usStockRepository;

    private final KrStockRepository krStockRepository;

    @Bean
    public StockDataUsecase<UsStock, UsStockRepository> usStockDataUsecase() {
        return new StockDataUsecase<>(usStockRepository);
    }

    @Bean
    public StockDataUsecase<KrStock, KrStockRepository> krStockDataUsecase() {
        return new StockDataUsecase<>(krStockRepository);
    }

    @Bean
    public OauthClient kakaoOauthClient(
            @Value("${oauth.kakao.base_url}") String baseUrl,
            @Value("${oauth.kakao.client_id}") String clientId,
            @Value("${oauth.kakao.auth_path}") String authPath
    ) {
        return new OauthClient(
                baseUrl,
                clientId,
                authPath,
                null
        );
    }

    @Bean
    public OauthClient naverOauthClient(
            @Value("${oauth.naver.base_url}") String baseUrl,
            @Value("${oauth.naver.client_id}") String clientId,
            @Value("${oauth.naver.auth_path}") String authPath
    ) {
        return new OauthClient(
                baseUrl,
                clientId,
                authPath,
                null
        );
    }

    @Bean
    public OauthClient googleOauthClient(
            @Value("${oauth.google.base_url}") String baseUrl,
            @Value("${oauth.google.client_id}") String clientId,
            @Value("${oauth.google.auth_path}") String authPath,
            @Value("${oauth.google.scope}") String scope

    ) {
        return new OauthClient(
                baseUrl,
                clientId,
                authPath,
                scope
        );
    }

}
