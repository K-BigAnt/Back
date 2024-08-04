package com.bigant.gaeme.dao;

import com.bigant.gaeme.dao.dto.UsEtfResponseDto;
import com.bigant.gaeme.dao.dto.UsStockDto;
import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
@RequiredArgsConstructor
public class UsStockDao implements StockDao<UsStockItem> {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.nasdaq.com")
            .build();

    private static final String STOCK_PATH = "/api/screener/stocks?download=true";

    private static final String ETF_PATH = "/api/screener/etf?download=true";

    @Override
    public List<UsStockItem> getStock() {
        ResponseEntity<UsStockDto> response = restClient.get().uri(STOCK_PATH)
                .retrieve()
                .toEntity(UsStockDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("미국 주식 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody().getData().getRows();
    }

    @Override
    public List<UsStockItem> getEtf() {
        ResponseEntity<UsEtfResponseDto> response = restClient.get().uri(ETF_PATH)
                .retrieve()
                .toEntity(UsEtfResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("미국 주식 정보를 가져오는데 실패했습니다.");
        }

        response.getBody().getData().getData().getRows().forEach(row ->
            row.setCountry("United States")
        );

        return response.getBody().getData().getData().getRows();
    }

}
