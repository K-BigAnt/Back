package com.bigant.gaeme.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StockPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate businessDate;

    private Long closePrice;

    private Long openPrice;

    private Long highestPrice;

    private Long lowestPrice;

    private Long accumulatedVolume;

    private Long accumulatedTradingAmount;

    private String previousDayContrastTodaySign;

    private Long previousDayContrastTodayPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stock stock;

}
