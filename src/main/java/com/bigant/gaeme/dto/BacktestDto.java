package com.bigant.gaeme.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BacktestDto {

    PortfolioDto.PortfolioStockDto stock;

    List<BacktestPriceDto> earns;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BacktestPriceDto {

        private LocalDate date;


        private Long amount;
    }


}
