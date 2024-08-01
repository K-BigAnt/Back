package com.bigant.gaeme.dao.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KrStockResponseDto {

    @Data
    @NoArgsConstructor
    public static class KrStockBody {

        public KrStockItems items;

    }

    @Data
    @NoArgsConstructor
    public static class KrStockItems {

        public List<KrStockDto> item;

    }

    @Data
    @NoArgsConstructor
    public static class KrStockResponse {

        public KrStockBody body;

    }

    public KrStockResponse response;

}
