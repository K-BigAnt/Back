package com.bigant.gaeme.config;

import com.bigant.gaeme.dao.OauthClient;
import com.bigant.gaeme.modelmapper.KrStockToStockSearchDtoConverter;
import com.bigant.gaeme.modelmapper.StockPriceDtoToStockPriceConverter;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.usecase.StockDataUsecase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

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
            @Value("${oauth.kakao.auth_url}") String authUrl,
            @Value("${oauth.kakao.token_url}") String tokenUrl,
            @Value("${oauth.kakao.info_url}") String infoUrl,
            @Value("${oauth.kakao.client_id}") String clientId,
            @Value("${oauth.kakao.client_secret}") String clientSecret,
            @Value("${oauth.kakao.redirect_uri}") String redirectUri
    ) {
        return new OauthClient(
                clientId,
                clientSecret,
                null,
                authUrl,
                tokenUrl,
                infoUrl,
                redirectUri
        );
    }

    @Bean
    public OauthClient naverOauthClient(
            @Value("${oauth.naver.auth_url}") String authUrl,
            @Value("${oauth.naver.token_url}") String tokenUrl,
            @Value("${oauth.naver.info_url}") String infoUrl,
            @Value("${oauth.naver.client_id}") String clientId,
            @Value("${oauth.naver.client_secret}") String clientSecret,
            @Value("${oauth.naver.redirect_uri}") String redirectUri
    ) {
        return new OauthClient(
                clientId,
                clientSecret,
                null,
                authUrl,
                tokenUrl,
                infoUrl,
                redirectUri
        );
    }

    @Bean
    public OauthClient googleOauthClient(
            @Value("${oauth.google.auth_url}") String authUrl,
            @Value("${oauth.google.token_url}") String tokenUrl,
            @Value("${oauth.google.info_url}") String infoUrl,
            @Value("${oauth.google.client_id}") String clientId,
            @Value("${oauth.google.client_secret}") String clientSecret,
            @Value("${oauth.google.scope}") String scope,
            @Value("${oauth.google.redirect_uri}") String redirectUri

    ) {
        return new OauthClient(
                clientId,
                clientSecret,
                scope,
                authUrl,
                tokenUrl,
                infoUrl,
                redirectUri
        );
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new KrStockToStockSearchDtoConverter());
        modelMapper.addConverter(new KrStockToStockSearchDtoConverter());
        modelMapper.addConverter(new StockPriceDtoToStockPriceConverter());

        return modelMapper;
    }

}
