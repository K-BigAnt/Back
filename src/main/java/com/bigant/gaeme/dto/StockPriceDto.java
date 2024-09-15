package com.bigant.gaeme.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDate;
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

    @JsonAlias({"closePrice", "stck_clpr"})
    private String closePrice;

    @JsonAlias({"openPrice", "stck_oprc"})
    private String openPrice;

    @JsonAlias({"highest_price", "stck_hgpr"})
    private String highestPrice;

    @JsonAlias({"lowestPrice", "stck_lwpr"})
    private String lowestPrice;

    @JsonAlias({"accumulatedVolume", "acml_vol"})
    private String accumulatedVolume;

    @JsonAlias({"accumulatedTradingAmount", "acml_tr_pbmn"})
    private String accumulatedTradingAmount;

    @JsonAlias({"previousDayContrastTodaySign", "prdy_vrss_sign"})
    private String previousDayContrastTodaySign;

    @JsonAlias({"previousDayContrastTodayPrice", "prdy_vrss"})
    private String previousDayContrastTodayPrice;

}
