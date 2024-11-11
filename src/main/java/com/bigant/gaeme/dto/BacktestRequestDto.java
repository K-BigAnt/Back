package com.bigant.gaeme.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacktestRequestDto {

    private PortfolioDto portfolio;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long initialAmount;

    private boolean isRebalanced;

}
