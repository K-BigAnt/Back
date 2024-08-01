package com.bigant.gaeme.dao;

import com.bigant.gaeme.dao.dto.KrStockDto;
import com.bigant.gaeme.dao.dto.KrStockResponseDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
@RequiredArgsConstructor
public class KrStockDao implements StockDao {

    private final String BASE_URL = "https://apis.data.go.kr";

    private final RestClient restClient = RestClient.builder().build();

    @Value("${stock.data.kr.api_key}")
    private String serviceKey;

    @Override
    public List<KrStockDto> getStock() {
        final String uriPath = "/1160100/service/GetKrxListedInfoService/getItemInfo";
        String uriString = String.format(
                BASE_URL + uriPath + "?serviceKey=%s&numOfRows=10000&pageNo=1&resultType=json&basDt=%s",
                serviceKey,
                getCurrentDateString());
        URI uri = getUri(uriString);
        ResponseEntity<KrStockResponseDto> response = restClient.get().uri(uri)
                .retrieve()
                .toEntity(KrStockResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("한국 주식 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody().response.body.items.item;
    }

    private URI getUri(String uriString) {
        URI uri;

        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("한국 주식 URI 변환에 실패했습니다.");
        }
        return uri;
    }

    private String getCurrentDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return LocalDate.now().minusDays(1).format(formatter);
    }

    @Override
    public List<KrStockDto> getEtf() {
        final String uriPath = "/1160100/service/GetSecuritiesProductInfoService/getETFPriceInfo";
        String uriString = String.format(
                BASE_URL + uriPath + "?serviceKey=%s&numOfRows=10000&pageNo=1&resultType=json&basDt=%s",
                serviceKey,
                getCurrentDateString());
        URI uri = getUri(uriString);
        ResponseEntity<KrStockResponseDto> response = restClient.get().uri(uri)
                .retrieve()
                .toEntity(KrStockResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("한국 주식 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody().response.body.items.item;
    }

}
