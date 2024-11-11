package com.bigant.gaeme.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDto {

    private String name;

    private List<PortfolioStockDto> stocks;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PortfolioStockDto {

        private String symbol;

        private Integer rate;

    }

}
