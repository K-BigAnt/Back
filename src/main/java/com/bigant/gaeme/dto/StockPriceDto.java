package com.bigant.gaeme.dto;

import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.entity.StockPrice;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockPriceDto {

    @JsonAlias({"businessDate", "stck_bsop_date"})
    private String businessDate;

    @JsonAlias({"closePrice", "stck_clpr", "ovrs_nmix_prpr"})
    private String closePrice;

    @JsonAlias({"openPrice", "stck_oprc", "ovrs_nmix_oprc"})
    private String openPrice;

    @JsonAlias({"highest_price", "stck_hgpr", "ovrs_nmix_hgpr"})
    private String highestPrice;

    @JsonAlias({"lowestPrice", "stck_lwpr", "ovrs_nmix_lwpr"})
    private String lowestPrice;

    @JsonAlias({"accumulatedVolume", "acml_vol"})
    private String accumulatedVolume;

    @JsonAlias({"accumulatedTradingAmount", "acml_tr_pbmn"})
    private String accumulatedTradingAmount;

    @JsonAlias({"previousDayContrastTodaySign", "prdy_vrss_sign"})
    private String previousDayContrastTodaySign;

    @JsonAlias({"previousDayContrastTodayPrice", "prdy_vrss"})
    private String previousDayContrastTodayPrice;

    @JsonIgnore
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public StockPrice toEntity(Stock stock) {
        if (stock instanceof KrStock) {
            return StockPrice.builder()
                    .businessDate(LocalDate.parse(getBusinessDate(), formatter))
                    .closePrice(Long.parseLong(getClosePrice()))
                    .openPrice(Long.parseLong(getOpenPrice()))
                    .highestPrice(Long.parseLong(getHighestPrice()))
                    .lowestPrice(Long.parseLong(getLowestPrice()))
                    .accumulatedVolume(Long.parseLong(getAccumulatedVolume()))
                    .accumulatedTradingAmount(Long.parseLong(getAccumulatedTradingAmount()))
                    .previousDayContrastTodaySign(getPreviousDayContrastTodaySign())
                    .previousDayContrastTodayPrice(Long.parseLong(getPreviousDayContrastTodayPrice()))
                    .stock(stock)
                    .build();
        }
        return StockPrice.builder()
                .businessDate(LocalDate.parse(getBusinessDate(), formatter))
                .closePrice(Math.round(Double.parseDouble(getClosePrice()) * 1300L))
                .openPrice(Math.round(Double.parseDouble(getOpenPrice()) * 1300L))
                .highestPrice(Math.round(Double.parseDouble(getHighestPrice()) * 1300L))
                .lowestPrice(Math.round(Double.parseDouble(getLowestPrice()) * 1300L))
                .accumulatedVolume(Long.parseLong(getAccumulatedVolume()))
                .accumulatedTradingAmount(null)
                .previousDayContrastTodaySign(null)
                .previousDayContrastTodayPrice(null)
                .stock(stock)
                .build();
    }

}
