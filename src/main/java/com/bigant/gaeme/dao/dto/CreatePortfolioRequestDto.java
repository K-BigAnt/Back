package com.bigant.gaeme.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePortfolioRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleStockDto {

        private String symbol;

        private Integer rate;

    }

    private String name;

    private List<SimpleStockDto> stocks;

}
