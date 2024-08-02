package com.bigant.gaeme.dao;

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

    @Override
    public List<UsStockItem> getStock() {
        ResponseEntity<UsStockDto> response = restClient.get().uri("/api/screener/stocks?download=true")
                .retrieve()
                .toEntity(UsStockDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("미국 주식 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody().getData().getRows();
    }

    @Override
    public List<UsStockItem> getEtf() {
        return null;
    }

}
