package com.bigant.gaeme.dao;

import com.bigant.gaeme.dto.StockPriceResponseDto;
import com.bigant.gaeme.dto.TokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Repository
public class StockPriceDao {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://openapi.koreainvestment.com:9443")
            .build();

    private final String appKey;

    private final String appSecret;

    private final ObjectMapper objectMapper;

    public StockPriceDao(@Value("${stock.data.app_key}") String appKey, @Value("${stock.data.app_secret}") String appSecret, ObjectMapper objectMapper) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.objectMapper = objectMapper;
    }

    public StockPriceResponseDto getKrStockPrice(LocalDate startDate, LocalDate endDate, String isinCode) {
        ResponseEntity<StockPriceResponseDto> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
                        .queryParams(getQueryParams(startDate, endDate, isinCode))
                .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("appkey", appKey)
                .header("appsecret", appSecret)
                .header("tr_id", "FHKST03010100")
                .retrieve()
                .toEntity(StockPriceResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("주식 가격 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody();
    }

    private String getToken() {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appKey);
        body.put("appsecret", appSecret);

        ResponseEntity<TokenResponseDto> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("oauth2/tokenP").build())
                .body(body)
                .retrieve()
                .toEntity(TokenResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("한국증권 토근 발급에 실패했습니다.");
        }

        return response.getBody().getAccessToken();
    }

    private MultiValueMap<String, String> getQueryParams(LocalDate startDate, LocalDate endDate, String isinCode) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        queryParams.add("FID_COND_MRKT_DIV_CODE", "J"); // 시장 구분 코드
        queryParams.add("FID_INPUT_DATE_1", startDate.format(formatter));
        queryParams.add("FID_INPUT_DATE_2", endDate.format(formatter));
        queryParams.add("FID_PERIOD_DIV_CODE", "M"); // 기간분류코드
        queryParams.add("FID_ORG_ADJ_PRC", "1"); // 수정주가 원주가 가격 여부
        queryParams.add("FID_INPUT_ISCD", isinCode.substring(1)); // 맨 앞의 A제거 추후에 db에 애초에 A 제거하고 넣어야함.

        return queryParams;
    }

}
